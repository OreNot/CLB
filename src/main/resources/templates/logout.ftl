<#import "parts/common.ftl" as c>

<@c.page>

<br>


<script language="JavaScript">
    <!-- hide
    function openNewWin(url) {
        myWin= open(url);
    }

</script>


<br>
<div>
    <#if error?has_content>
        ${error}
    </#if>
</div>
<br>

</div>
</div>
<br>
LOGOUT

</@c.page>