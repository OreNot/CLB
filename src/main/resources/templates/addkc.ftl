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
                <h4 class="display-4">Добавление Ключевых носителей для <#if type == "arm">АРМ </#if><#if arm??>${arm.armName}</#if></h4>
                <div class="jumbotron">
                    <div class="form-group row mt-3">
                        <div class="col-sm-9">
                            <#if kcname??>
                                <input type="text" class="form-control" name="kcname" placeholder="Наименование Ключевых носителей" value="${kcname}"/>
                            <#else>
                                <input type="text" class="form-control" name="kcname" placeholder="Наименование Ключевых носителей"/>
                            </#if>
                        </div>
                    </div>

                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="kctype" id="kctype" placeholder="Тип ключевых носителей">
                            <option value="Тип ключевых носителей">Тип ключевых носителей</option>
                            <#list kctypes as kctype>
                                <option value="${kctype.id}" <#if selectedkctype??><#if kctype.name == "${selectedkctype}">selected="selected"</#if></#if>>${kctype.name}</option>
                            </#list>

                        </select>

                    </div>

                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить ключевые носители</button>

                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button class="btn btn-secondary mb-2" onclick="window.open('${urlprefixPath}/showkcs');window.close(this)">Выйти без изменения</button>
                    </div>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>
    <br>


</@c.page>