<#import "parts/common.ftl" as c>

<@c.page>

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
            <h4 class="display-4">Добавление Организации</h4>
            <div class="jumbotron">
                <h4 class="display-5">Реквизиты</h4>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if organizationname??>
                        <input type="text" class="form-control" name="organizationname" placeholder="Наименование" value="${organizationname}"/>
                    <#else>
                        <input type="text" class="form-control" name="organizationname" placeholder="Наименование"/>
                    </#if>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if gid??>
                        <input type="text" class="form-control" name="gid" placeholder="GID" value="${gid}"/>
                    <#else>
                        <input type="text" class="form-control" name="gid" placeholder="GID"/>
                    </#if>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if inn??>
                        <input type="text" class="form-control" name="inn" placeholder="ИНН" value="${inn}"/>
                    <#else>
                        <input type="text" class="form-control" name="inn" placeholder="ИНН"/>
                    </#if>
                </div>
            </div>

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
                    <#if position??>
                        <input type="text" class="form-control" name="position" placeholder="Должность" value="${position}"/>
                    <#else>
                        <input type="text" class="form-control" name="position" placeholder="Должность"/>
                    </#if>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if phonenumber??>
                        <input type="text" class="form-control" name="phonenumber" placeholder="Тел. номер" value="${phonenumber}"/>
                    <#else>
                        <input type="text" class="form-control" name="phonenumber" placeholder="Тел. номер"/>
                    </#if>
                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-9">
                    <#if email??>
                        <input type="text" class="form-control" name="email" placeholder="E-mail" value="${email}"/>
                    <#else>
                        <input type="text" class="form-control" name="email" placeholder="E-mail"/>
                    </#if>
                </div>
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
                    <button type="submit" class="btn btn-primary mb-2">Добавить организацию</button>

                </div>
            </div>

            <div class="form-group row mt-3">
                <div class="col-sm-3">
                    <button class="btn btn-secondary mb-2" onclick="window.open('${urlprefixPath}/showorganizations');window.close(this)">Выйти без изменения</button>
                </div>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}">

        </form>
    </div>
</div>
<br>


</@c.page>