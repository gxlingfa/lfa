<!DOCTYPE html>
    <html>
<style type="text/css">
    table.gridtable {
        font-family: verdana,arial,sans-serif;
        font-size:11px;
        color:#333333;
        border-width: 1px;
        border-color: #666666;
        border-collapse: collapse;
    }
    table.gridtable th {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #666666;
        background-color: #dedede;
    }
    table.gridtable td {
        border-width: 1px;
        padding: 8px;
        border-style: solid;
        border-color: #666666;
        background-color: #ffffff;
    }
</style>
    <body>
    <table class="gridtable">
        <thead><th>序号</th><th>区域</th><th>标题</th><th>公告时间</th></thead>
        <tbody>
        <#if notices?exists >
            <#list notices as item>
        <tr><td>${item_index+1}</td>
            <td>${item.source.desc}</td>
            <td><a href="${item.url}">${item.title}</a> </td>
            <td>${item.releaseDate? datetime}</td>
        </tr>
            </#list>
            </#if>
        </tbody>
    </table>
    </body>
    </html>