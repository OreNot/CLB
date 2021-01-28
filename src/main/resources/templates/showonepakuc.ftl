<#import "parts/common.ftl" as c>


<@c.page>

    <br>
    <script src="/js/scripts.js"></script>
    <br>
    <div>
        <#if error?has_content>
            ${error}
        </#if>
    </div>
    <br>
    <!--
    <div class="form-group row mt-3">
        <div class="col-sm-3">
            <button class="btn btn-primary mb-2" onclick="window.close()">Закрыть</button>

        </div>
    </div>
    -->

    <div class="form-row">
        <div class="form-group col-md-10">
            <form method="post" enctype="multipart/form-data" id="js-upload-form">

                <div><h3 class="display-4">ПАК УЦ <#if pakuc?has_content>${pakuc.pakUCName}</#if></h3></div>


                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col"><button class="btn btn-primary mb-2" onclick="window.close(this);window.open('${urlprefixPath}/adddocument?id=${pakuc.id}&type=pakuc')">Добавить документ</button></th>
                        <th scope="col"><button class="btn btn-secondary mb-2" onclick="window.close()">Выйти без изменения</button></th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-light">

                    </tr>

                    </tbody>
                </table>

                <br>
                <div class="jumbotron">
                    <h4 class="display-5">Документы ПАК УЦ</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Номер документа</th>
                            <th scope="col">Дата документа</th>
                            <th scope="col">Срок действия</th>
                            <th scope="col">Реквизиты</th>
                            <th scope="col">Тип</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list documents as document>

                            <tr class="table-light"
                                onclick="window.open('${urlprefixPath}/showonedocument?docid=${document.id}')">

                                <td><#if document.documentNumber??>${document.documentNumber}</#if></td>
                                <td>${document.documentDate}</td>
                                <td><#if document.validUntilDate != "01.01.3020">${document.validUntilDate}<#else>Бессрочно</#if></td>
                                <td>${document.parameters}</td>
                                <td>${document.documenttype.name}</td>
                                <td><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${document.id}&type=document&returntype=pakuc&returnid=${pakuc.id}')"></td>


                            </tr>
                        <#else>
                            Пусто
                        </#list>
                        </tbody>
                    </table>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>

    <br>


</@c.page>