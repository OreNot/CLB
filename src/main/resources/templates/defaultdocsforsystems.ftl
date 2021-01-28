<#import "parts/common.ftl" as c>


<@c.page>

    <br>
    <script src="/js/scripts.js"></script>
    <script language="JavaScript">
        function toggleSelect(elem) {

            document.getElementById("submit").click();
        };

        function adddocforsystem(elem) {
            var el = document.getElementById("systemid");
            var elValue = el.options[el.selectedIndex].value;
            window.close(this);window.open('${urlprefixPath}/adddocument?id=' + elValue + '&type=systemname');
        }
    </script>


    <br>
    <div>
        <#if error?has_content>
            ${error}
        </#if>
    </div>
    <br>



    <div class="form-row">
        <div class="form-group col-md-10">
            <form method="post" enctype="multipart/form-data" id="js-upload-form">


                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col"><button class="btn btn-primary mb-2" onclick="adddocforsystem(this)">Добавить документ</button></th>
                        <th scope="col"><button class="btn btn-secondary mb-2" onclick="window.close()">Выйти без изменения</button></th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-light">

                    </tr>

                    </tbody>
                </table>

                <br>
                <div class="form-check form-check-inline mt-3">

                    <select class="form-control" style="width:auto;" name="systemid" id="systemid" placeholder="Тип системы" onchange="toggleSelect(this)">
                        <#list systemnames as systemname>
                            <option value="${systemname.id}" <#if selectedsystemname??><#if systemname.name == "${selectedsystemname}">selected="selected"</#if></#if>>${systemname.name}</option>
                        </#list>

                    </select>

                </div>
                <br>
                <br>


                <div class="jumbotron">
                    <h4 class="display-5">Документы системы</h4>

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
                                <td><#if document.validUntilDate != "3020-01-01">${document.validUntilDate}<#else>Бессрочно</#if></td>
                                <td>${document.parameters}</td>
                                <td>${document.documenttype.name}</td>
                                <td><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${document.id}&type=document&returntype=systemnames&returnid=${selectedsystemname}')"></td>

                            </tr>
                        <#else>
                            Пусто
                        </#list>
                        </tbody>
                    </table>
                </div>
                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" id="submit" hidden class="btn btn-primary mb-2"></button>

                    </div>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>

    <br>


</@c.page>