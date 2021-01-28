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
                <h4 class="display-4">Добавление СЗИ для <#if type == "arm">АРМ </#if><#if type == "bank">банка </#if><#if arm??>${arm.armName}</#if><#if organization??>${organization.name}</#if></h4>
                <div class="jumbotron">


                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="sziid" id="sziid" placeholder="СЗИ">
                            <option value="0">СЗИ</option>
                            <#list szis as szi>
                                <option value="${szi.id}" <#if selectedszi??><#if szi.sziName == "${selectedszi}">selected="selected"</#if></#if>>${szi.sziName}</option>
                            </#list>

                        </select>

                    </div>

                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить СЗИ</button>

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