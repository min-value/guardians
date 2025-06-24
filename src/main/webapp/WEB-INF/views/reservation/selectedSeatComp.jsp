<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String zoneColor = request.getParameter("zoneColor");
    String zoneName = request.getParameter("zoneName");
    String zoneDetail = request.getParameter("zoneDetail");
%>
<div class="selectedSeatComp-container">
    <div class="selectedSeatComp-wrapper">
        <div class="selectedSeatComp-zone-color-wrapper">
            <div class="selectedSeatComp-zone-color" style="background-color: <%=zoneColor%>">
            </div>
        </div>
        <div class="selectedSeatComp-zone-name-wrapper">
            <div class="selectedSeatComp-zone-name">
                <%=zoneName%>
            </div>
        </div>
        <div class="selectedSeatComp-zone-detail-wrapper">
            <div class="selectedSeatComp-zone-detail">
                <%=zoneDetail%>
            </div>
        </div>
    </div>
</div>