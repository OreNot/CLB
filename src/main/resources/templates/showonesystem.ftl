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

            <div><h3 class="display-4">Система <#if system?has_content>${system.name}, Вид электронной подписи: ${system.estype.name}</#if></h3></div>



            <table class="table">
                <thead>
                <tr>
                    <th scope="col"><button class="btn btn-primary mb-2" onclick="window.close(this);window.open('${urlprefixPath}/adddocument?id=${system.id}&type=system')">Добавить документ</button></th>
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
                <h4 class="display-5">Параметры системы</h4>

                <div class="jumbotron">
                    Тип системы : ${system.systemName.name}
                </div>
                <div class="jumbotron" style="background-color: #ffdf7e">
                    Банк : ${system.bank.name}
                    <br>
                    <#if bankarms??>
                    Арм Банка :
                        <br>
                        <#list bankarms as bankarm>
                            - ${bankarm.armType.name} : ${bankarm.armName}
                            <br>
                            <#else>
                            Пусто
                        </#list>
                    </#if>

                </div>
                <div class="jumbotron" style="background-color: #b8daff">
                    Организация : ${system.organization.name}
                    <br>
                    <#if organizationarms??>
                        Арм Организации :
                        <br>
                        <#list organizationarms as organizationarm>
                            - ${organizationarm.armType.name} : ${organizationarm.armName}
                            <br>
                        <#else>
                            Пусто
                        </#list>
                    </#if>
                </div>
                <div class="jumbotron" style="background-color: #17a2b8">
                    Тип системы: ${system.systemType.name}
                    <br>
                    Тип подписи : ${system.esType.name}
                    <br>
                    Срок действия ключа ЭП : ${system.keyExpirationMonths}
                    <br>
                    Тип шифрования при передачи через сеть интернет : ${system.cryptoType}
                    <br>
                    <#if system.bankSoftware?? && system.bankSoftware != false>Система является собственной разработкой банка</#if>

                </div>
            </div>

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
                        <td><#if document.documentDate??>${document.documentDate}</#if></td>
                        <td><#if document.validUntilDate??><#if document.validUntilDate != "01.01.3020">${document.validUntilDate}<#else>Бессрочно</#if></#if></td>
                        <td><#if document.parameters??>${document.parameters}</#if></td>
                        <td><#if document.documenttype??>${document.documenttype.name}</#if></td>
                        <td><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${document.id}&type=document&returntype=system&returnid=${system.id}')"></td>

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