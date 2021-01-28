<#import "parts/common.ftl" as c>


<@c.page>

<br>


<script language="JavaScript">
    <!-- hide
    function openNewWin(url) {
        myWin= open(url);
    }

</script>

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

            <div><h3>Заключение № <#if report?has_content>${report.documentNumber} от ${report.documentDate}</#if></h3></div>



            <table class="table">
                <thead>
                <tr>
                    <th scope="col"><button class="btn btn-primary mb-2" onclick="window.close(this);window.open('${urlprefixPath}/changebank?tid=${bank.id}')">Изменить данные банка</button></th>
                    <th scope="col"><button class="btn btn-primary mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addcontact?tid=${bank.id}&type=bank')">Добавить контактное лицо</button></th>
                    <th scope="col"><button class="btn btn-primary mb-2" onclick="window.close(this);window.open('${urlprefixPath}/addskzi?tid=${bank.id}&type=bank&mode=add')">Добавить СКЗИ</button></th>
                    <th scope="col"><button class="btn btn-primary mb-2" onclick="window.close(this);window.open('${urlprefixPath}/adddocument?tid=${bank.id}&type=bank')">Добавить документ</button></th>
                    <th scope="col"><button class="btn btn-primary mb-2" onclick="window.close()">Выйти без изменения</button></th>
                </tr>
                </thead>
                <tbody>

                <tr class="table-light">

                </tr>

                </tbody>
            </table>
            <!--
            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button type="submit" class="btn btn-primary mb-2">Изменить данные банка</button>

                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/addcontact?tid=${bank.id}')">Добавить контактное лицо</button>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/adddocument?tid=${bank.id}')">Добавить документ</button>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-primary mb-2" onclick="window.close()">Выйти без изменения</button>
                </div>
            </div>
-->
            <br>

            <div><h4>Контактные лица</h4></div>
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
                        onclick="window.open('${urlprefixPath}/showonecontact?tid=${contact.id}')">

                        <td>${contact.fio}</td>
                        <td>${contact.position}</td>
                        <td>${contact.phoneNumber}</td>
                        <td>${contact.email}</td>
                        <td><input type="button" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/delorgcontact?contactid=${contact.id}&orgid=${bank.id}&type=bank')"></td>

                    </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>


            <br>
            <div><h4>СКЗИ банка</h4></div>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Наименование СКЗИ</th>
                    <th scope="col">Действие</th>
                </tr>
                </thead>
                <tbody>
                    <#list skzis as skzi>

                    <tr class="table-light"
                        onclick="window.open('${urlprefixPath}/showoneskzi?skziid=${skzi.id}')"
                    >

                        <td><#if skzi.name??>${skzi.name}</#if></td>
                        <td><input type="button" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/delskzi?skziid=${skzi.id}&tid=${tid}&type=bank')"></td>

                    </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>

            <br>
            <div><h4>Документы банка</h4></div>
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
                    <!--onclick="window.open('${urlprefixPath}/showonedocument?tid=${document.id}')"> -->

                    <td><#if document.documentNumber??>${document.documentNumber}</#if></td>
                    <td>${document.documentDate}</td>
                    <td>${document.validUntilDate}</td>
                    <td>${document.parameters}</td>
                    <td>${document.documenttype.name}</td>
                    <td><input type="button" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/delorgdocument?docid=${document.id}&orgid=${bank.id}&type=bank')"></td>

                    </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>

<br>


</@c.page>