<#import "parts/common.ftl" as c>


<@c.page>

<br>
    <script src="/js/scripts.js"></script>
<script>
    function toggleSelect(elem) {

        if(elem.value == "Выполнено")
        {
            document.getElementById('report').hidden = false;
            document.getElementById('reportlabel').hidden = false;
        }
        else
        {
            document.getElementById('report').hidden = true;
            document.getElementById('reportlabel').hidden = true;
        }
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

            <div><h3 class="display-4">Банк <#if bank?has_content>${bank.name}</#if></h3></div>



            <table class="table">
                <thead>
                <tr>
                    <th scope="col"><button class="btn btn-light mb-2" onclick="window.close(this);window.open('${urlprefixPath}/changebank?id=${bank.id}')">Изменить данные банка</button></th>
                    <th scope="col"><button class="btn btn-secondary mb-2" onclick="window.close()">Выйти без изменения</button></th>
                </tr>
                </thead>
                <tbody>

                    <tr class="table-light">

                    </tr>

                </tbody>
            </table>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addcontact?id=${bank.id}&type=bank')">Добавить контактное лицо</button></th>
                    <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addarm?id=${bank.id}&type=bank')">Добавить АРМ</button></th>
                    <th scope="col"><button class="btn btn-success mb-2" onclick="window.close(this);window.open('${urlprefixPath}/adddocument?id=${bank.id}&type=bank')">Добавить документ</button></th>
                </tr>
                </thead>
                <tbody>

                <tr class="table-light">

                </tr>

                </tbody>
            </table>


            <br>

            <div class="jumbotron">
                <h4 class="display-5">Контактные лица</h4>

            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Ф.И.О.</th>
                    <th scope="col">Должность</th>
                    <th scope="col">Телефон</th>
                    <th scope="col">E-mail</th>
                    <th scope="col">Действие</th>
                </tr>
                </thead>
                <tbody>
                    <#list contacts as contact>

                    <tr class="table-light"
                        onclick="window.open('${urlprefixPath}/showonecontact?id=${contact.id}')">

                        <td>${contact.fio}</td>
                        <td><#if contact.position??>${contact.position}</#if></td>
                        <td><#if contact.phoneNumber??>${contact.phoneNumber}</#if></td>
                        <td><#if contact.email??>${contact.email}</#if></td>
                        <td><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${contact.id}&type=contact&returntype=bank&returnid=${bank.id}')"></td>

                    </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>
            </div>

            <div class="jumbotron">
                <h4 class="display-5">АРМ банка</h4>
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Наименование АРМ</th>
                        <th scope="col">Тип АРМ</th>
                        <th scope="col">Действие</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list armList as arm>

                        <tr class="table-light"
                            onclick="window.open('${urlprefixPath}/showonearm?id=${arm.id}&sourcetype=bank&sourceid=${bank.id}')"
                        >

                            <td><#if arm.armName??>${arm.armName}</#if></td>
                            <td><#if arm.armType.name??>${arm.armType.name}</#if></td>
                            <td><#if arm??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${arm.id}&type=arm&returntype=bank&returnid=${bank.id}')"></#if></td>

                        </tr>
                    <#else>
                        Пусто
                    </#list>
                    </tbody>
                </table>
            </div>


            <div class="jumbotron">
                <h4 class="display-5">Документы банка</h4>

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
                        onclick="window.open('${urlprefixPath}/showonedocument?docid=${document.id}')" >
                        <td><#if document.documentNumber??>${document.documentNumber}</#if></td>
                        <td>${document.documentDate}</td>
                        <td><#if document.validUntilDate != "01.01.3020">${document.validUntilDate}<#else>Бессрочно</#if></td>
                        <td><#if document.parameters != "0">${document.parameters}<#else> </#if></td>
                        <td>${document.documenttype.name}</td>
                        <td><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${document.id}&type=document&returntype=bank&returnid=${bank.id}')"></td>

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