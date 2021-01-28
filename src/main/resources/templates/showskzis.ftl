<#import "parts/common.ftl" as c>


<@c.page>

<br>
<script src="/js/scripts.js"></script>
<div class="form-row">
    <div class="form-group col-md-10">
        <form method="post" enctype="multipart/form-data" id="js-upload-form">


            <table class="table">
                <thead>
                <tr>
                    <th scope="col"><button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/addskzi');window.close(this)">Добавить СКЗИ</button></th>
                </tr>
                </thead>
                <tbody>

                <tr class="table-light">

                </tr>

                </tbody>
            </table>

            <br>

            <div class="jumbotron">
    <h4 class="display-5">СКЗИ</h4>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Наименование СКЗИ</th>
            <th scope="col">Версия СКЗИ</th>
            <th scope="col">Вариант исполнения</th>
            <th scope="col">Уровень</th>
            <th scope="col">Действие</th>
        </tr>
        </thead>
        <tbody>
        <#list skzis as skzi>

            <tr class="table-light">

                <td  onclick="window.close(this);window.open('${urlprefixPath}/showoneskzi?skziid=${skzi.id}')"><#if skzi.name??>${skzi.name}</#if></td>
                <td><#if skzi.version??>${skzi.version}</#if></td>
                <td><#if skzi.realizationVariant??>${skzi.realizationVariant}</#if></td>
                <td><#if skzi.KS??><#if skzi.KS == "KS1">КС1<#elseif skzi.KS == "KS2">КС2</#if></#if></td>
                <td><#if skzi??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${skzi.id}&type=skzi&returntype=skzis')"></#if></td>

            </tr>
        <#else>
            Пусто
        </#list>
        </tbody>
    </table>
</div>
        </form>
    </div>
</div>
</@c.page>