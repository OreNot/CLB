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

            <div><h3>Добавление используемых СКЗИ в заключение</h3></div>

            <div class="form-check form-check-inline mt-3">

                <select class="form-control" name="bankskzi" id="bankskzi" placeholder="СКЗИ Банка">
                    <option value="Клиент">СКЗИ Банка</option>
                    <#if bankskzis??>
                    <#list bankskzis as bankskzi>
                        <option value="${bankskzi.id}">${bankskzi.name}</option>
                    </#list>
                    <#else>
                    Пусто
                    </#if>


                </select>

            </div>

            <br>

            <div class="form-check form-check-inline mt-3">

                <select class="form-control" name="bankid" id="bankid" placeholder="СКЗИ Клиента">
                    <option value="Банк">СКЗИ Клиента</option>
                    <#if organizationskzis??>
                    <#list organizationskzis as organizationskzi>
                        <option value="${organizationskzis.id}">${organizationskzis.name}</option>
                    </#list>
                    </#if>

                </select>

            </div>


            <br>

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