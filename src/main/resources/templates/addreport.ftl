<#import "parts/common.ftl" as c>

<@c.page>

<br>

<script src="/js/scripts.js"></script>

<script language="JavaScript">

</script>
<script>
    function toggleSelect(elem) {

        document.getElementById("submit").click();
    }

</script>

<br>
<div>
    <#if error?has_content>
        <p class="text-info">${error}</p>
    </#if>
</div>
<br>
<div class="form-row">
    <div class="form-group col-md-10">
        <form method="post" enctype="multipart/form-data" id="js-upload-form">

            <div><h3 class="display-4">Добавление заключения</h3></div>

            <div class="jumbotron">
                <h1 class="display-4">Выбор контрагентов</h1>
                <br>
                <p class="lead"> Информация о банке</p>

                <div class="form-check form-check-inline mt-3">

                    <select class="form-control" name="bank" id="bank" placeholder="Банк" onchange="toggleSelect(this)">
                        <option value="0">Банк</option>
                        <#list banks as bank>
                            <option value="${bank.id}" <#if selectedbank??><#if bank.name == "${selectedbank}">selected="selected"</#if></#if>>${bank.name}</option>
                        </#list>
                    </select>
                </div>

                <hr class="my-4">
                <p class="lead"> Информация о клинте</p>
                <div class="form-check form-check-inline mt-3">

                    <select class="form-control" name="organization" id="organization" placeholder="Клиент" onchange="toggleSelect(this)">
                        <option value="0">Клиент</option>
                        <#list organizations as organization>
                            <option value="${organization.id}" <#if selectedorganization??><#if organization.name == "${selectedorganization}">selected="selected"</#if></#if>>${organization.name}</option>
                        </#list>

                    </select>

                </div>

                <#if systems??>
                <hr class="my-4">
                <p class="lead"> Информация о системе</p>
                <div class="form-check form-check-inline mt-3">

                    <select class="form-control" name="system" id="system" placeholder="Система">
                        <option value="Система">Система</option>
                        <#list systems as system>
                            <option <#if selectedsystem?has_content && system  == "${selectedsystem}">selected</#if> value="${system.id}">${system.name}</option>

                        </#list>

                    </select>

                </div>
                </#if>

            </div>


            <div class="jumbotron">
                <h1 class="display-4">Реквизиты</h1>
                         <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if documentnumber??>
                        <input type="text" class="form-control" name="documentnumber" placeholder="№ документа" value="${documentnumber}"/>
                    <#else>
                        <input type="text" class="form-control" name="documentnumber" placeholder="№ документа"/>
                    </#if>
                </div>
            </div>


            <div class="form-group row mt-3">
                <div class="col-auto">
                    <label for="inputDate">Дата документа</label>
                    <#if documentdate??>
                        <input type="date" class="form-control" name="documentdate" placeholder="Дата документа" value="${documentdate}"/>
                    <#else>
                        <input type="date" class="form-control" name="documentdate" placeholder="Дата документа"/>
                    </#if>
                </div>
            </div>


            <div class="form-group row mt-3">
                <div class="col-auto">
                    <label for="inputDate">Срок действия</label>
                    <#if validuntildate??>
                        <input type="date" class="form-control" name="validuntildate" placeholder="Срок действия" value="${validuntildate}"/>
                    <#else>
                        <input type="date" class="form-control" name="validuntildate" placeholder="Срок действия"/>
                    </#if>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if parameters??>
                        <input type="text" class="form-control" name="parameters" placeholder="Реквизиты" value="${parameters}"/>
                    <#else>
                        <input type="text" class="form-control" name="parameters" placeholder="Реквизиты"/>
                    </#if>
                </div>
            </div>
                <#if trustlevels??>
                    <hr class="my-4">
                    <p class="lead"> Уровень доверия</p>
                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" name="trustLevel" id="trustLevel" placeholder="Уровень доверия">
                            <option value="Уровень доверия">Уровень доверия</option>
                            <#list trustlevels as trustlevel>
                                <option <#if selectedtrustLevel?has_content && trustlevel  == "${selectedtrustLevel}">selected</#if> value="${trustlevel.id}">${trustlevel.name}</option>

                            </#list>

                        </select>

                    </div>
                </#if>
            </div>






            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button type="submit" id="submit" class="btn btn-primary mb-2">Добавить документ</button>

                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-primary mb-2" onclick="window.close();window.open('${urlprefixPath}/showreports')">Выйти без изменения</button>
                </div>
            </div>



            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>