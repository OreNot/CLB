<#import "parts/common.ftl" as c>

<@c.page>

<br>

<script src="/js/scripts.js"></script>

<script language="JavaScript">
    <!-- hide

    function toggleCheckbox(elem) {

        if(elem.checked) {
            document.getElementById('validuntildatediv').hidden = true;
        }
        else
        {
            document.getElementById('validuntildatediv').hidden = false;
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

            <div><h3>Добавление документа для <#if system?has_content>${system.name}</#if></h3></div>


            <div class="form-check form-check-inline mt-3">

                <select class="form-control" name="documenttype" id="documenttype" placeholder="Тип документа">
                    <option value="Тип Документа">Тип Документа</option>
                    <#list documenttypes as documenttype>
                        <option value="${documenttype.id}" <#if selectedexecutor??><#if documenttype.name == "${documenttype}">selected="selected"</#if></#if>>${documenttype.name}</option>
                    </#list>

                </select>

            </div>

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
                        <input type="date" class="form-control" name="documentdate" placeholder="Дата документа"  value="${documentdate}"/>
                    <#else>
                        <input type="date" class="form-control" name="documentdate" placeholder="Дата документа"/>
                    </#if>
                </div>
            </div>


            <div id="validuntildatediv" class="form-group row mt-3">
                <div class="col-auto">
                    <label for="inputDate">Срок действия</label>
                    <#if validuntildate??>
                        <input type="date" class="form-control" name="validuntildate" placeholder="Срок действия" value="${validuntildate}"/>
                    <#else>
                        <input type="date" class="form-control" name="validuntildate" placeholder="Срок действия"/>
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

            <div class="file-upload-wrapper mb-2">
                <input type="file"  name="file" id="input-file-now" class="file-upload"/>
                <label class="input-file-label" for="input-file-now">Документы</label>
            </div>

            <!--
            <div class="form-check form-check-inline mt-3">

                <select class="form-control" name="status" id="status" placeholder="Статус">
                    <option value="Статус Документа">Статус Документа</option>
                    <#list statuses as status>
                        <option>${status.name}</option>
                    </#list>

                </select>

            </div>
            -->
            <br>


            <!--
        <div class="form-group row mt-3">
            <div class="col-sm-9">
                <#if fio??>
                <input type="text" class="form-control" name="fio" placeholder="ФИО" value="${fio}"/>
                <#else>
                    <input type="text" class="form-control" name="fio" placeholder="ФИО"/>
                </#if>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-9">
                <#if organization??>
                <input type="text" class="form-control" name="organization" placeholder="Организация" value="${organization}"/>
                    <#else>
                        <input type="text" class="form-control" name="organization" placeholder="Организация"/>
                </#if>
            </div>
        </div>

        <div class="form-group row mt-3">
            <div class="col-sm-3">
                <#if catnum??>
                <input type="text" class="form-control" name="catnum" placeholder="Номер папки" value="${catnum}">
                <#else>
                    <input type="text" class="form-control" name="catnum" placeholder="Номер папки">
                </#if>
            </div>
        </div>

        <div class="file-upload-wrapper mb-2">
            <input type="file"  name="file" id="input-file-now" class="file-upload"/>
            <label class="input-file-label" for="input-file-now">Документы</label>
        </div>
-->
            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button type="submit" class="btn btn-primary mb-2">Добавить документ</button>

                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-primary mb-2" onclick="window.close()">Выйти без изменения</button>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>