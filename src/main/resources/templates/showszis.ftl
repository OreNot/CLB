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
                        <th scope="col"><button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/addszi');window.close(this)">Добавить СЗИ</button></th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-light">

                    </tr>

                    </tbody>
                </table>

                <br>

                <div class="jumbotron">
                    <h4 class="display-5">СЗИ</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Наименование СЗИ</th>
                            <th scope="col">Уровень</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list szis as szi>

                            <tr class="table-light">

                                <td onclick="window.open('${urlprefixPath}/showoneszi?id=${szi.id}')"><#if szi.sziName??>${szi.sziName}</#if></td>
                                <td><#if szi.KS2?has_content><#if szi.KS2 != "0">КС2</#if></#if></td>
                                <td><#if szi??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${szi.id}&type=szi&returntype=szis')"></#if></td>

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