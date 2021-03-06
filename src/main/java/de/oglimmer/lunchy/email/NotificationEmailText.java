package de.oglimmer.lunchy.email;

import java.text.MessageFormat;
import java.util.List;

import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.MailImage;
import de.oglimmer.lunchy.rest.dto.UpdatesQuery;
import de.oglimmer.lunchy.rest.resources.PictureResource;

public class NotificationEmailText {

	private static String CR = "\r\n";
	private static String BR = "<br/>";

	private static final String HEAD_STYLE = "a {color:black; } a.plain {text-decoration: none; } .caption { margin-bottom: 20px;font-style: italic; } .food { border:2px solid black; } @media only screen and (max-width: 800px) { .food { width:95%; } } @media only screen and (min-width: 800px) { #main { width:800px; } } ";
	private static final String BODY_STYLE = "background: rgba(141,215,255,1);background: -moz-linear-gradient(top, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);background: -webkit-gradient(left top, left bottom, color-stop(0%, rgba(141,215,255,1)), color-stop(100%, rgba(175,204,220,1)));background: -webkit-linear-gradient(top, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);background: -o-linear-gradient(top, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);background: -ms-linear-gradient(top, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);background: linear-gradient(to bottom, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);filter: progid:DXImageTransform.Microsoft.gradient( startColorstr=\"#8dd7ff\", endColorstr=\"#afccdc\", GradientType=0 );font-family:\"Helvetica Neue\", Helvetica, Arial, sans-serif;font-size:18px";
	private static final String CONTENT_DIV_STYLE = "margin-bottom:50px;padding:0px 20px 20px 20px;background-color:#f6f6ef;margin-left:auto;margin-right:auto;border:3px solid black;border-radius:8px;-webkit-box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);-moz-box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);";

	private static final String greeting = "Hello {0} my hungry friend,";
	private static final String top_intro1 = "This is the weekly update from {0}.";
	private static final String top_intro2 = "Here are {0} new pictures which have been added last week: ";
	private static final String middle_text = "Besides of these pictures, this happened last week at {0}:";
	private static final String end_text1 = "Check out new lunch places at {0} today or contribute with your own pictures and reviews.";
	private static final String end_text2 = "Btw, lunchy works on your mobile as well - you can even upload pictures from a mobile.";
	private static final String cheers = "Regards,";
	private static final String sender = "Oli";
	private static final String unsubscribe_line = "To unsubscribe from this email change your settings at {0}";
	private static final String no_updates = "Nothing happened this week :( - visit lunchy now and enter new locations, reviews and pictures!";

	public static String getHtml(UsersRecord rec, List<UpdatesQuery> updates, List<MailImage> images) {
		StringBuilder body = new StringBuilder("<html><head><meta charset='utf-8'><style>" + HEAD_STYLE + "</style></head><body style='" + BODY_STYLE
				+ "'>");
		body.append("<div id='main' style='" + CONTENT_DIV_STYLE + "'>");
		body.append("<h2>" + MessageFormat.format(greeting, rec.getDisplayname()) + "</h2>").append(BR);
		if (updates.isEmpty()) {
			body.append(BR + no_updates + BR);
		} else {
			body.append(MessageFormat.format(top_intro1, getHtmlUrl(rec.getFkCommunity())));
			body.append(BR);
			body.append(BR);
			body.append(MessageFormat.format(top_intro2, images.size()));
			body.append(BR);
			body.append(BR);
			body.append("<div>");
			boolean evenRow = false;
			for (MailImage mi : images) {
				body.append("<div " + (evenRow ? "style='float: right;'" : "") + ">");
				body.append("<a class='plain' href='" + EmailProvider.INSTANCE.getUrl(rec.getFkCommunity()) + "/#/view/"
						+ mi.getId() + "?pic=" + mi.getPictureId() + "'>");
				addImage(body, mi, rec.getFkCommunity());
				if (mi.getCaption() != null && !mi.getCaption().isEmpty()) {
					body.append("<div class='caption'>" + mi.getDisplayname() + ": " + mi.getCaption() + "</div>");
				} else {
					body.append("<div class='caption'>" + mi.getDisplayname() + " made this at " + mi.getOfficialName()
							+ "</div>");
				}
				body.append("</a>");
				body.append("</div>");
				evenRow = !evenRow;
			}
			body.append("</div>");
			body.append(BR);
			body.append("<hr style='clear: both;visibility:hidden;'/>");
			body.append(MessageFormat.format(middle_text, getHtmlUrl(rec.getFkCommunity())));
			body.append(BR);
			body.append("<ul>");
			for (UpdatesQuery up : updates) {
				body.append(
						"<li><a class='plain' href='" + EmailProvider.INSTANCE.getUrl(rec.getFkCommunity()) + "/#/" + up.getRef()
								+ "'>").append(up.getText()).append("</a></li>");
			}
			body.append("</ul>");
		}
		body.append(BR);
		body.append(MessageFormat.format(end_text1, getHtmlUrl(rec.getFkCommunity())));
		body.append(BR);
		body.append(BR);
		body.append(end_text2);
		body.append(BR);
		body.append(BR);
		body.append(cheers).append(BR);
		body.append(sender).append(BR);
		body.append(BR);
		body.append(BR);
		body.append(MessageFormat.format(unsubscribe_line, getHtmlUrl(rec.getFkCommunity())));
		body.append("</div></body></html>");
		return body.toString();
	}

	private static Object getHtmlUrl(int fkCommunity) {
		String link = EmailProvider.INSTANCE.getUrl(fkCommunity);
		String domain = EmailProvider.INSTANCE.getDomain(fkCommunity);
		return "<a href='" + link + "'>" + domain + "</a>";
	}

	public static String getText(UsersRecord rec, List<UpdatesQuery> updates, List<MailImage> images) {
		StringBuilder body = new StringBuilder();
		body.append(MessageFormat.format(greeting, rec.getDisplayname())).append(CR);
		body.append(CR);
		if (updates.isEmpty()) {
			body.append(CR + no_updates + CR);
		} else {
			body.append(MessageFormat.format(top_intro1, EmailProvider.INSTANCE.getUrl(rec.getFkCommunity())));
			body.append(CR);
			body.append(MessageFormat.format(top_intro2, images.size()));
			body.append(CR);
			body.append(CR);
			body.append(MessageFormat.format(middle_text, EmailProvider.INSTANCE.getUrl(rec.getFkCommunity())));
			body.append(CR);
			body.append(CR);
			for (UpdatesQuery up : updates) {
				body.append("* ").append(up.getText());
			}
		}
		body.append(CR);
		body.append(MessageFormat.format(end_text1, EmailProvider.INSTANCE.getUrl(rec.getFkCommunity())));
		body.append(CR);
		body.append(CR);
		body.append(end_text2);
		body.append(CR);
		body.append(CR);
		body.append(cheers).append(CR);
		body.append(sender).append(CR);
		body.append(CR);
		body.append(CR);
		body.append(MessageFormat.format(unsubscribe_line, EmailProvider.INSTANCE.getUrl(rec.getFkCommunity())));
		return body.toString();
	}

	private static void addImage(StringBuilder body, MailImage mi, int fkCommunity) {
		body.append("<img src=\"" + getBasePicUrl(fkCommunity) + mi.getFilename() + "?size=" + PictureResource.SMALL_PIC_BOUNDRY
				+ "\" class='food' />");
	}

	private static String getBasePicUrl(int fkCommunity) {
		return EmailProvider.INSTANCE.getUrl(fkCommunity) + "/rest/pictures/";
	}
}
