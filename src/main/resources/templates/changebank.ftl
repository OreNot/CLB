<#import "parts/common.ftl" as c>

<@c.page>
<img src="${urlprefixPath}/img/greenatom.png" class="rounded float-left" width="145" height="159">
<br>
    <script src="/js/scripts.js"></script>
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

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if bank.name??>
                        <input type="text" class="form-control" name="bankname" placeholder="Наименование" value="${bank.name}"/>
                    <#else>
                        <input type="text" class="form-control" name="bankname" placeholder="Наименование"/>
                    </#if>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if bank.inn??>
                        <input type="text" class="form-control" name="inn" placeholder="ИНН" value="${bank.inn}"/>
                    <#else>
                        <input type="text" class="form-control" name="inn" placeholder="ИНН"/>
                    </#if>
                </div>
            </div>



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
                    <button type="submit" class="btn btn-primary mb-2">Изменить</button>

                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>