<%--

<jsp:useBean id="bookDB" class="com.sun.bookstore2.database.BookDB" scope="page" >

    <jsp:setProperty name="bookDB" property="database" value="${bookDBAO}" />

</jsp:useBean>


<c:if test="${param.Clear == 'clear'}">

    <font color="red" size="+2"><strong>

            <fmt:message key="CartCleared"/>

    </strong><br>&nbsp;<br></font>

</c:if>


<c:if test="${param.Remove != '0'}">

    <c:set var="bid" value="${param.Remove}"/>

    <jsp:setProperty name="bookDB" property="bookId" value="${bid}" />
    <c:set var="removedBook" value="${bookDB.book}" />

    <font color="red" size="+2"><fmt:message key="CartRemoved"/><em>${removedBook.title}</em>.

        <br>&nbsp;<br>

    </font>

</c:if>

--%>


<c:if test="${sessionScope.cart.numberOfItems > 0}"> 

    <font size="+2"><fmt:message key="CartContents"/>

        ${sessionScope.cart.numberOfItems}

        <c:if test="${sessionScope.cart.numberOfItems == 1}">

            <fmt:message key="CartItem"/>.

        </c:if>

        <c:if test="${sessionScope.cart.numberOfItems > 1}">

            <fmt:message key="CartItems"/>.

        </c:if>

    </font><br>&nbsp;


    <table summary="layout">

        <tr>

            <th align=left><fmt:message key="ItemQuantity"/></th>

            <th align=left><fmt:message key="ItemTitle"/></th>

            <th align=left><fmt:message key="ItemPrice"/></th>

        </tr>


        <c:forEach var="item" items="${sessionScope.cart.items}">

            <c:set var="book" value="${item.item}" />

            <tr>

                <td align="right" bgcolor="#ffffff">

                    ${item.quantity}

                </td>


                <td bgcolor="#ffffaa">

                    <c:url var="url" value="/books/bookdetails" >

                        <c:param name="bookId" value="${book.bookId}" />

                        <c:param name="Clear" value="0" />

                    </c:url>

                    <strong><a href="${url}">${book.title}</a></strong>

                </td>


                <td bgcolor="#ffffaa" align="right">

                    <fmt:formatNumber value="${book.price}" type="currency"/>&nbsp;

                </td>


                <td bgcolor="#ffffaa">

                    <c:url var="url" value="/books/bookshowcart" >

                        <c:param name="Remove" value="${book.bookId}" />

                    </c:url>

                    <strong><a href="${url}"><fmt:message key="RemoveItem"/></a></strong>

                </td>

            </tr>

        </c:forEach>


        <tr>
            <td colspan="5" bgcolor="#ffffff">

                <br>
            </td>
        </tr>


        <tr>

            <td colspan="2" align="right" bgcolor="#ffffff">

                <fmt:message key="Subtotal"/>

            </td>

            <td bgcolor="#ffffaa" align="right">

                <fmt:formatNumber value="${sessionScope.cart.total}" type="currency"/>

            </td>

            <td><br></td>

        </tr>
    </table>


    <p>&nbsp;<p>

    <c:url var="url" value="/books/bookcatalog" >

        <c:param name="Add" value="" />

    </c:url>

    <strong><a href="${url}"><fmt:message key="ContinueShopping"/></a>&nbsp;&nbsp;&nbsp;

        <c:url var="url" value="/books/bookcashier" />

        <a href="${url}"><fmt:message key="Checkout"/></a>&nbsp;&nbsp;&nbsp;

        <c:url var="url" value="/books/bookshowcart" >

            <c:param name="Clear" value="clear" />

            <c:param name="Remove" value="0" />

        </c:url>

    <a href="${url}"><fmt:message key="ClearCart"/></a></strong>

</c:if>



<c:if test="${sessionScope.cart.numberOfItems <= 0}"> 

    <font size="+2"><fmt:message key="CartEmpty"/></font>

    <br>&nbsp;<br>

    <c:url var="url" value="/books/bookcatalog" >

        <c:param name="Add" value="" />

    </c:url>

    <strong><a href="${url}"><fmt:message key="Catalog"/></a></strong>

</c:if>



</body>

</html>



