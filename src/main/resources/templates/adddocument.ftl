<#import "parts/common.ftl" as c>

<@c.page>

<br>
    <script src="/js/scripts.js"></script>

<script language="JavaScript">

    function toggleCheckbox(elem) {

        if(elem.checked) {
            document.getElementById('validuntildatediv').hidden = true;
        }
        else
            {
                document.getElementById('validuntildatediv').hidden = false;
            }
        }

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

            <div><h3 class="display-4">Добавление документа для <#if objectName?has_content>${objectName}</#if></h3></div>


                <h4 class="display-5">Реквизиты</h4>

            <div class="form-check form-check-inline mt-3">

                <select class="form-control" style="width:auto;" name="documenttype" id="documenttype" placeholder="Тип документа" onchange="toggleSelect(this)">
                    <option value="Тип Документа">Тип Документа</option>
                    <#list documenttypes as documenttype>
                        <option value="${documenttype.id}" <#if selecteddocumenttype??><#if documenttype.name == "${selecteddocumenttype}">selected="selected"</#if></#if>>${documenttype.name}</option>
                    </#list>

                </select>

            </div>
            <br>
            <br>
            <div class="jumbotron mt-1">
         <#if dopparams??>
                <div class="jumbotron">



             <#if  dopparams == "51">
                 <div class="form-check form-check-inline mt-3">

                     <select class="form-control" style="width:auto;" name="bankid" id="bankid" placeholder="Банк">
                         <option value="Банк">Банк</option>
                         <#list banks as bank>
                             <option value="${bank.id}" <#if selectedbank??><#if bank.name == "${selectedbank}">selected="selected"</#if></#if>>${bank.name}</option>
                         </#list>

                     </select>

                 </div>
             </#if>

         <#if  dopparams == "35">

             <div class="form-group row mt-3">
                 <div class="col-sm-9">
                     <#if sziname??>
                         <input type="text" class="form-control" name="sziname" placeholder="Наименование СЗИ из сертификата соотвествия" value="${sziname}"/>
                     <#else>
                         <input type="text" class="form-control" name="sziname" placeholder="Наименование СЗИ из сертификата соотвествия"/>
                     </#if>
                 </div>
             </div>

             <div class="form-group row mt-3">
                 <div class="col-sm-9">
                     <#if sziversion??>
                         <input type="text" class="form-control" name="sziversion" placeholder="Версия СЗИ из сертификата соотвествия" value="${sziversion}"/>
                     <#else>
                         <input type="text" class="form-control" name="sziversion" placeholder="Версия СЗИ из сертификата соотвествия"/>
                     </#if>
                 </div>
             </div>

         </#if>

             <#if dopparams == "1" || dopparams == "2">
                 <div class="form-group row mt-3">
                     <div class="col-sm-9">
                         <#if keyinftool??>
                             <input type="text" class="form-control" name="keyinftool" placeholder="Наименование средства, реализующего инф. ключей системы" value="${keyinftool}"/>
                         <#else>
                             <input type="text" class="form-control" name="keyinftool" placeholder="Наименование средства, реализующего инф. ключей системы"/>
                         </#if>
                     </div>
                 </div>
             </#if>

                    <#if dopparams == "7" || dopparams == "27">
                        <div class="form-group row mt-3">
                            <div class="col-sm-9">
                                <#if keyinftool??>
                                    <input type="text" class="form-control" name="tokentypename" placeholder="Наименование ключевого ностителя" value="${tokentypename}"/>
                                <#else>
                                    <input type="text" class="form-control" name="tokentypename" placeholder="Наименование ключевого ностителя"/>
                                </#if>
                            </div>
                        </div>
                    </#if>

                </div>

         </#if>

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
                <div class="col-sm-9">
                    <#if documentname??>
                        <input type="text" class="form-control" name="documentname" placeholder="Наименование документа" value="${documentname}"/>
                    <#else>
                        <input type="text" class="form-control" name="documentname" placeholder="Наименование документа"/>
                        <small id="validuntildateHelp" class="form-text text-muted">* Важно для Договора между ГА и клиентом</small>
                        <small id="validuntildateHelp" class="form-text text-muted">* Важно для Договора между банком и клиентом</small>
                        <small id="validuntildateHelp" class="form-text text-muted">* Важно для Договора на использование средства, реализующего инфраструктуру ключевой системы</small>
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


            <div id="validuntildatediv" class="form-group row mt-3">
                <div class="col-auto">
                    <label for="inputDate">Срок действия</label>
                    <#if validuntildate??>
                        <input type="date" class="form-control" id="validuntildate" name="validuntildate" placeholder="Срок действия" value="${validuntildate}"/>
                    <#else>
                        <input type="date" class="form-control" id="validuntildate" name="validuntildate" placeholder="Срок действия"/>
                    </#if>
                </div>
            </div>

            <div class="custom-control custom-checkbox">
                <input type="checkbox" class="custom-control-input" id="unlimiteddate" name="unlimiteddate" onchange="toggleCheckbox(this)">
                <label class="custom-control-label" for="unlimiteddate">Бессрочно</label>
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

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if link??>
                        <input type="text" class="form-control" name="link" placeholder="Ссылка" value="${link}"/>
                    <#else>
                        <input type="text" class="form-control" name="link" placeholder="Ссылка"/>
                    </#if>
                </div>
            </div>

            <div class="file-upload-wrapper mb-2">
                <input type="file"  name="file" id="input-file-now" class="file-upload"/>
                <label class="input-file-label" for="input-file-now">Документы</label>
            </div>


            </div>



            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button type="submit" id="submit" class="btn btn-primary mb-2">Добавить документ</button>

                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-secondary mb-2" onclick="window.close();window.open('${urlprefixPath}/<#if type == "systemname">defaultdocsforsystems<#else>showone${type}?id=${id}</#if>')">Выйти без изменения</button>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>