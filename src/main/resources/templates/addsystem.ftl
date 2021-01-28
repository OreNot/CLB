<#import "parts/common.ftl" as c>

<@c.page>

<br>
    <script src="/js/scripts.js"></script>
    <script language="JavaScript">
    function toggleSelect(elem) {

    document.getElementById("submit").click();
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


            <div class="form-check form-check-inline mt-3">

                <select class="form-control" name="bankid" id="bankid" placeholder="Банк" onchange="toggleSelect(this)">
                    <option value="0">Банк</option>
                    <#list banks as bank>
                        <option value="${bank.id}" <#if selectedbank??><#if bank.name == "${selectedbank}">selected="selected"</#if></#if>>${bank.name}</option>
                    </#list>

                </select>

            </div>

            <#if selectedbank?? && selectedbank != "Банк">
                <br>
            <div class="form-check form-check-inline mt-3">

                <select class="form-control" name="systemnameid" id="systemnameid" placeholder="Система" onchange="toggleSelect(this)">
                    <option value="0">Система</option>
                    <#list systemnames as systemname>
                        <option value="${systemname.id}" <#if selectedsystemname??><#if systemname.name == "${ selectedsystemname}">selected="selected"</#if></#if>>${systemname.name}</option>
                    </#list>

                </select>

            </div>
            </#if>
             <#if selectedsystemname?? && selectedsystemname != "Система">
                <br>
                 <#if organizations??>
                 <div class="form-check form-check-inline mt-3">

                     <select class="form-control" name="organizationid" id="organizationid" placeholder="Организация" onchange="toggleSelect(this)">
                         <option value="0">Организация</option>
                         <#list organizations as organization>
                             <option value="${organization.id}" <#if selectedorganization??><#if organization.name == "${selectedorganization}">selected="selected"</#if></#if>>${organization.name}</option>
                         </#list>

                     </select>

                 </div>
                </#if>
                 <br>
                 <div class="form-group row mt-3">
                     <div class="col-sm-9">
                         <#if systemname??>
                             <input type="text" class="form-control" name="systemname" placeholder="Наименование" value="${systemname}"/>
                         <#else>
                             <input type="text" class="form-control" name="systemname" placeholder="Наименование"/>
                         </#if>
                     </div>
                 </div>
            <#if selectedorganization?? && selectedorganization != "Организация">
            <div class="form-check form-check-inline mt-3">

                <select class="form-control" name="systemtype" id="systemtype" placeholder="Тип системы">
                    <option value="0">Тип системы</option>
                    <#list systemtypes as systemtype>
                        <option value="${systemtype.id}" <#if selectedsystemtype??><#if systemtype.name == "${selectedsystemtype}">selected="selected"</#if></#if>>${systemtype.name}</option>
                    </#list>

                </select>

            </div>
          <br>
            <div class="form-check form-check-inline mt-3">

                <select class="form-control" style="width:auto;" name="estype" id="estype" placeholder="Вид электронной подписи">
                    <option value="0">Вид электронной подписи</option>
                    <#list estypes as estype>
                        <option value="${estype.id}" <#if selectedestype??><#if estype.name == "${selectedestype}">selected="selected"</#if></#if>>${estype.name}</option>
                    </#list>

                </select>

            </div>

                 <div class="form-group row mt-3">
                     <div class="col-sm-3">
                         <input type="text" class="form-control" name="keyExpirationMonths" placeholder="Срок действия ключа ЭП"/>
                     </div>
                 </div>

                 <div class="form-check form-check-inline mt-3">

                     <select class="form-control" name="cryptoType" id="cryptoType" placeholder="Тип шифрования">
                         <option value="0">Тип шифрования</option>
                              <#if clientArmSKZI??>
                             <option value="Протокол TLS с применением алгоритма ГОСТ с использованием СКЗИ ${clientArmSKZI}" >Протокол TLS с применением алгоритма ГОСТ с использованием СКЗИ ${clientArmSKZI}</option>
                             </#if>
                             <option value="Протокол TLS с применением алгоритма RSA" >Протокол TLS с применением алгоритма RSA</option>
                     </select>

                 </div>

                 <div class="custom-control custom-checkbox mt-3">
                     <input type="checkbox" class="custom-control-input" value="banksoftware" id="banksoftware" name="banksoftware">
                     <label class="custom-control-label" for="banksoftware">Собственная разработка банка</label>
                 </div>

            </#if>

            </#if>


            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button type="submit" id="submit" class="btn btn-primary mb-2">Добавить систему</button>

                </div>
            </div>


            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/showsystems');window.close(this)">Выйти без изменения</button>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>