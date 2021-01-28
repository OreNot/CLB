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
                        <th scope="col"><button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/addpakuc');window.close(this)">Добавить ПАК УЦ</button></th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-light">

                    </tr>

                    </tbody>
                </table>

                <br>

                <div class="jumbotron">
                    <h4 class="display-5">ПАК УЦ</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Наименование ПАК УЦ</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list pakUCList as pakUC>

                            <tr class="table-light">

                                <td onclick="window.open('${urlprefixPath}/showonepakuc?id=${pakUC.id}')"><#if pakUC.pakUCName??>${pakUC.pakUCName}</#if></td>
                                <td><#if pakUC??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${pakUC.id}&type=pakuc&returntype=pakucs')"></#if></td>

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