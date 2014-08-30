package de.oglimmer.lunchy.web.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import lombok.Setter;

public class ScriptTag extends SimpleTagSupport {

	@Setter
	private String src;

	@Override
	public void doTag() throws JspException, IOException {
		Writer out = getJspContext().getOut();
		ReloadHash data = new ReloadHash(src, getJspContext());
		out.write("<script ");
		out.write("src=\"" + src + data.getReloadHashParameter() + "\" ");
		out.write("></script>");
	}

}