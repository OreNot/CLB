<#import "parts/common.ftl" as c>


<@c.page>

    <br>
    <script src="/js/scripts.js"></script>
    <div class="form-row">
        <div class="form-group col-md-10">
            <form method="post" enctype="multipart/form-data" id="js-upload-form">


                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col"><button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/addav');window.close(this)">Добавить Антивирус</button></th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-light">

                    </tr>

                    </tbody>
                </table>

                <br>

                <div class="jumbotron">
                    <h4 class="display-5">Антивирусы</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Наименование Антивируса</th>
                            <th scope="col">Версия Антивируса</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list antiviruses as antivirus>

                            <tr class="table-light">

                                <td  onclick="window.open('${urlprefixPath}/showoneantivirus?id=${antivirus.id}')"><#if antivirus.avName??>${antivirus.avName}</#if></td>
                                <td><#if antivirus.avVersion??>${antivirus.avVersion}</#if></td>
                                <td><#if antivirus??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${antivirus.id}&type=antivirus&returntype=antiviruses')"></#if></td>

                            </tr>
                        <#else>
                            Пусто
                        </#list>
                        </tbody>
                    </table>
                </div>
            </form>
        </div>
    </div>
</@c.page>