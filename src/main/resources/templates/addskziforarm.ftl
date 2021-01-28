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
                <h4 class="display-4">Добавление СКЗИ для <#if type == "arm">АРМ </#if><#if type == "bank">банка </#if><#if arm??>${arm.armName}</#if><#if organization??>${organization.name}</#if></h4>
                <div class="jumbotron">


                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="skziid" id="skziid" placeholder="СКЗИ">
                            <option value="0">СКЗИ</option>
                            <#list skzis as skzi>
                                <option value="${skzi.id}" <#if selectedskzi??><#if skzi.name == "${selectedskzi}">selected="selected"</#if></#if>>${skzi.name} ${skzi.version} ${skzi.realizationVariant} ${skzi.KS}</option>
                            </#list>

                        </select>

                    </div>

                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить СКЗИ</button>

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