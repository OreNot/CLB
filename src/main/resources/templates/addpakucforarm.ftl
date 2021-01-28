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
                <h4 class="display-4">Добавление ПАК УЦ для <#if type == "arm">АРМ </#if><#if type == "bank">банка </#if><#if arm??>${arm.armName}</#if><#if organization??>${organization.name}</#if></h4>
                <div class="jumbotron">


                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="pakucid" id="pakucid" placeholder="ПАК УЦ">
                            <option value="0">ПАК УЦ</option>
                            <#list pakucs as pakuc>
                                <option value="${pakuc.id}" <#if selectedpakuc??><#if pakuc.pakUCName == "${selectedpakuc}">selected="selected"</#if></#if>>${pakuc.pakUCName}</option>
                            </#list>

                        </select>

                    </div>

                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить ПАК УЦ</button>

                    </div>
                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button class="btn btn-secondary mb-2" onclick="window.close()">Выйти без изменения</button>
                    </div>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}">

            </form>
        </div>
    </div>
    <br>


</@c.page>