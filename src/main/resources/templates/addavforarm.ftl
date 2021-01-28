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
                <h4 class="display-4">Добавление антивируса для <#if type == "arm">АРМ </#if><#if type == "bank">банка </#if><#if arm??>${arm.armName}</#if><#if organization??>${organization.name}</#if></h4>
                <div class="jumbotron">


                    <div class="form-check form-check-inline mt-3">

                        <select class="form-control" style="width:auto;" name="avid" id="avid" placeholder="Антивирус">
                            <option value="0">Антивирус</option>
                            <#list antiviruses as antivirus>
                                <option value="${antivirus.id}" <#if selectedantivirus??><#if antivirus.avName == "${selectedantivirus}">selected="selected"</#if></#if>>${antivirus.avName}</option>
                            </#list>

                        </select>

                    </div>

                </div>

                <div class="form-group row mt-3">
                    <div class="col-sm-3">
                        <button type="submit" class="btn btn-primary mb-2">Добавить Антивирус</button>

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