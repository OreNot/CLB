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

            <div><h4 class="display-4">Банки</h4></div>
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Наименование</th>
                    <th scope="col">ИНН</th>
                    <th scope="col">Действие</th>
                </tr>
                </thead>
                <tbody>
                    <#list banks as bank>

                            <tr class="table-light"
                    onclick="window.close(this);window.open('${urlprefixPath}/showonebank?id=${bank.id}')">

                    <td>${bank.name}</td>
                    <td><#if bank.inn??>${bank.inn}</#if></td>
                    <td><#if bank??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${bank.id}&type=organization&returntype=banks')"></#if></td>

                            </tr>
                    <#else>
                    Пусто
                    </#list>
                </tbody>
            </table>

            <br>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/addbank');window.close(this)">Добавить банк</button>
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>