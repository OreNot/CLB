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
                        <th scope="col"><button class="btn btn-primary mb-2" onclick="window.open('${urlprefixPath}/addkc');window.close(this)">Добавить ключевые носители</button></th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-light">

                    </tr>

                    </tbody>
                </table>

                <br>

                <div class="jumbotron">
                    <h4 class="display-5">Ключевые носители</h4>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col">Наименование Ключевых носителей</th>
                            <th scope="col">Тип Ключевых носителей</th>
                            <th scope="col">Действие</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list keyCarriersList as keyCarriers>

                            <tr class="table-light" >

                                <td onclick="window.open('${urlprefixPath}/showonekeycarriers?id=${keyCarriers.id}')"><#if keyCarriers.keyCarriersName??>${keyCarriers.keyCarriersName}</#if></td>
                                <td><#if keyCarriers??>${keyCarriers.keyCarriersType.name}</#if></td>
                                <td><#if keyCarriers??><input type="button" class="btn btn-danger" value="Удалить" onclick="window.close(this);window.open('${urlprefixPath}/deleteelement?id=${keyCarriers.id}&type=keycarriers&returntype=keycarrierses')"></#if></td>

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