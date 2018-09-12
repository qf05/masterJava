<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" omit-xml-declaration="yes" indent="yes"/>
    <xsl:param name="projectName" />
    <xsl:template match="/">
        <html>
            <head>
            </head>
            <body>
                <h1><xsl:value-of select="$projectName"/></h1>
                <table border="1" style="font-size:21px;">
                    <tr>
                        <th>groupName</th>
                        <th>status</th>
                    </tr>
                    <xsl:for-each select="/*[name()='Payload']/*[name()='Projects']/*[name()='Project'][@name=$projectName]/*[name()='Groups']/*[name()='Group']">
                        <tr>
                            <td>
                                <xsl:value-of select="@name"/>
                            </td>
                            <td>
                                <xsl:value-of select="@type"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>