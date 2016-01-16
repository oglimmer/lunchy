package de.oglimmer.lunchy.web.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import de.oglimmer.lunchy.database.dao.UsageDao;
import lombok.Setter;

public class LogTag extends SimpleTagSupport {

	@Setter
	private String type;

	@Override
	public void doTag() throws JspException, IOException {
		HttpServletRequest request = ((HttpServletRequest) ((PageContext) getJspContext()).getRequest());
		UsageDao.INSTANCE.store("pageLoad", type, request, null);
	}

}