<%--
    Document   : cart
    Created on : Feb 18, 2009, 7:07:03 PM
    Author     : nbuser
--%>


<%@page contentType="text/html; charset=UTF-8"%> <%-- required for '➟' and '€' signs --%>

<jsp:useBean id="affableBeanDB" class="database.AffableBeanDB">
    <jsp:setProperty name="affableBeanDB" property="database" value="${affableBeanDBAO}" />
</jsp:useBean>

<%-- Set session-scoped variable to track the view user is coming from.
     This is used by the language mechanism in the Dispatcher so that
     users view the same page when switching between EN and CS. --%>
<c:set var="view" value="/cart" scope="session" />

<c:set var="cart" value="${sessionScope.cart}"/>


<%-- HTML markup starts below --%>

<div id="cartColumn">

    <c:choose>
        <c:when test="${cart.numberOfItems > 1}">
            <p><fmt:message key="yourCartContains" /> ${cart.numberOfItems} <fmt:message key="items" />.</p>
        </c:when>
        <c:when test="${cart.numberOfItems == 1}">
            <p><fmt:message key="yourCartContains" /> ${cart.numberOfItems} <fmt:message key="item" />.</p>
        </c:when>
        <c:otherwise>
            <p><fmt:message key="yourCartEmpty" /></p>
        </c:otherwise>
    </c:choose>

    <div id="actionBar">
        <%-- clear cart widget --%>
        <c:if test="${cart.numberOfItems != 0}">

            <c:url var="url" value="viewCart">
                <c:param name="clear" value="true"/>
            </c:url>

            <a href="${url}" class="rounded bubble hMargin"><fmt:message key="clearCart" /></a>
        </c:if>

        <%-- continue shopping widget --%>
        <c:set var="value">
            <c:choose>
                <%-- if 'selectedCategory' session object exists, send user to previously viewed category --%>
                <c:when test="${!empty sessionScope.selectedCategory}">
                    category
                </c:when>
                <%-- otherwise send user to welcome page --%>
                <c:otherwise>
                    index.jsp
                </c:otherwise>
            </c:choose>
        </c:set>

        <c:url var="url" value="${value}" />
        <a href="${url}" class="rounded bubble hMargin"><fmt:message key="continueShopping" /></a>

        <%-- checkout widget --%>
        <c:if test="${cart.numberOfItems != 0}">
            <a href="<c:url value="checkout"/>" class="rounded bubble hMargin"><fmt:message key="proceedCheckout" /></a>
        </c:if>
    </div>

    <c:if test="${cart.numberOfItems != 0}">

        <h4 id="subtotal"><fmt:message key="subtotal" />: € <c:out value="${cart.total}"/></h4>

        <table id="cartTable">

            <tr class="header">
                <th><fmt:message key="product" /></th>
                <th><fmt:message key="name" /></th>
                <th><fmt:message key="price" /></th>
                <th><fmt:message key="quantity" /></th>
            </tr>

            <c:forEach var="item" items="${cart.items}" varStatus="iter">

                <c:set var="product" value="${item.item}" />

                <tr>
                    <td class="${((iter.index % 2) == 0) ? 'even' : 'odd'}">
                        <img src="<c:out value="${product.imagePath}"/>"
                            alt="image of <fmt:message key="${product.name}"/>">
                    </td>
                    <td class="${((iter.index % 2) == 0) ? 'even' : 'odd'}">
                        <fmt:message key="${product.name}"/>
                    </td>
                    <td class="${((iter.index % 2) == 0) ? 'even' : 'odd'}">
                        € <c:out value="${item.total}"/>
                        <br>
                        <span class="smallText">( € <c:out value="${product.price}"/> / unit )</span>
                    </td>
                    <td class="${((iter.index % 2) == 0) ? 'even' : 'odd'}">

                        <form action="<c:url value="updateCart"/>" method="post">
                            <input type="hidden"
                                   name="productId"
                                   value="<c:out value="${product.id}"/>">
                            <input type="text"
                                   maxlength="2"
                                   size="2"
                                   value="${item.quantity}"
                                   name="quantity"
                                   style="margin:5px">
                            <input type="submit"
                                   name="submit"
                                   value="<fmt:message key="update"/>">
                        </form>

                    </td>
                </tr>

            </c:forEach>

        </table>

    </c:if>
</div>