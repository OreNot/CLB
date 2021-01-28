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

            <div><h4 class="display-4">Заключения</h4></div>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Номер документа</th>
                    <th scope="col">Дата документа</th>
                    <th scope="col">Срок действия документа</th>
                    <th scope="col">Реквизиты</th>
                    <th scope="col">Система</th>
                    <th scope="col">Статус системы</th>
                    <th scope="col">Килиент</th>
                    <th scope="col">Банк</th>
                    <th scope="col">Статус</th>
                    <th scope="col">Уровень доверия</th>
                </tr>
                </thead>
                <tbody>
                    <#list documents as document>

                    <!--<tr class="table-light"-->
                        <tr class=<#if document.lastdocumentstatusupdatestatus == "3">"table-danger"<#elseif document.lastdocumentstatusupdatestatus == "2">"table-warning"<#elseif document.lastdocumentstatusupdatestatus == "1">"table-primary"<#else>"table-light"</#if>
                        onclick="window.open('${urlprefixPath}/showonereport?reportid=${document.id}')">

                        <td>${document.documentnumber}</td>
                        <td>${document.documentdate}</td>
                        <td>${document.validuntildate}</td>
                        <td>${document.parameters}</td>
                        <td><#if document.system??>${document.system.name}<#else>Не указан</#if></td>
                        <td>${document.systemstatus.name}</td>
                        <td><#if document.organization??>${document.organization.name}<#else>Не указан</#if></td>
                        <td><#if document.bank??>${document.bank.name}<#else> </#if></td>
                        <td>${document.status.name}</td>
                        <td><span class="badge badge-pill <#if document.trustlevel.name  == "Высокий">badge-success<#elseif document.trustlevel.name  == "Средний">badge-info<#elseif document.trustlevel.name  == "Не указан">badge-dark<#else>badge-danger</#if>"><#if document.trustlevel??>${document.trustlevel.name}<#else>Не указан</#if></span></td>
                        <!--<td><#if document.trustlevel??>${document.trustlevel.name}<#else>Не указан</#if></td>-->
                    </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>

            <br>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/addreport');window.close(this)">Добавить заключение</button>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>