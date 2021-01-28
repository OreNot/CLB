<#import "parts/common.ftl" as c>

<@c.page>
<!--<img src="${urlprefixPath}/img/greenatom.png" class="rounded float-left" width="145" height="159">-->
<br>
    <script src="/js/scripts.js"></script>
<script>
    function radioClick(elem) {

        switch(elem.value) {
            case 'statusfilter':
                document.getElementById("status").disabled = false;
                document.getElementById("urgency").disabled = true;
                document.getElementById("workgroup").disabled = true;
                break;

            case 'urgencyfilter':
                document.getElementById("status").disabled = true;
                document.getElementById("urgency").disabled = false;
                document.getElementById("workgroup").disabled = true;
                break;

            case 'workgroupfilter':
                document.getElementById("status").disabled = true;
                document.getElementById("urgency").disabled = true;
                document.getElementById("workgroup").disabled = false;
                break;

            case 'executorfilter':
                document.getElementById("status").disabled = true;
                document.getElementById("urgency").disabled = true;
                document.getElementById("workgroup").disabled = true;
                document.getElementById("executor").disabled = false;
                break;

        }
    }
</script>
<script>
    function toggleSelect(elem) {

        document.getElementById("submit").click();
    }

    function toggleCheckbox(elem) {
        document.getElementById("submit").click();
    }
</script>

<div>
    <#if error?has_content>
        ${error}
    </#if>
</div>

<div class="form-row">
    <div class="form-group col-md-10">
        <form method="post" enctype="multipart/form-data" id="js-upload-form">




            <div><h4 class="display-4">Информационные системы</h4></div>
            <br>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col"><button class="btn btn-primary mb-2" onclick="openLink(event, 'http://${hostname}/${urlprefixPath}addsystem')">Добавить систему</button></th>
                    <th scope="col"> <button class="btn btn-primary mb-2" onclick="openLink(event, 'http://${hostname}/${urlprefixPath}defaultdocsforsystems')">Общие документы</button></th>
                </tr>
                </thead>
                <tbody>

                <tr class="table-light">

                </tr>

                </tbody>
            </table>

            <br>
            <table class="table">
                <thead>
                <tr>
                    <th class="table-warning" scope="col">Банк</th>
                    <th class="table-success" scope="col">Наименование</th>
                    <th class="table-primary" scope="col">Организация</th>
                    <th class="table-info" scope="col">Тип системы</th>
                    <th class="table-info" scope="col">Тип подписи</th>
                    <th class="table-info" scope="col">Действие</th>
                </tr>
                </thead>
                <tbody>
                    <#list systems as system>

                    <tr class="table-light">


                        <td onclick="window.open('${urlprefixPath}/showonesystem?id=${system.id}');window.close(this)">${system.bank.name}</td>
                        <td onclick="window.open('${urlprefixPath}/showonesystem?id=${system.id}');window.close(this)">${system.name}</td>
                        <td onclick="window.open('${urlprefixPath}/showonesystem?id=${system.id}');window.close(this)">${system.organization.name}</td>
                        <td onclick="window.open('${urlprefixPath}/showonesystem?id=${system.id}');window.close(this)">${system.systemType.name}</td>
                        <td onclick="window.open('${urlprefixPath}/showonesystem?id=${system.id}');window.close(this)">${system.esType.name}</td>

                        <td><#if system??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${system.id}&type=system&returntype=systems')"></#if></td>

                    </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>

            <br>


            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>