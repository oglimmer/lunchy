package de.oglimmer.lunchy.web.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import lombok.Setter;

public class StylesTag extends SimpleTagSupport {

	@Setter
	private String href;

	@Override
	public void doTag() throws JspException, IOException {
		Writer out = getJspContext().getOut();
		ReloadHash data = new ReloadHash(href, getJspContext());
		out.write("<link ");
		out.write("href=\"" + href + data.getReloadHashParameter() + "\" rel=\"stylesheet\"");
		out.write("/>");
	}

}
