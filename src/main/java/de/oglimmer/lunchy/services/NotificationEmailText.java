package de.oglimmer.lunchy.services;

import java.text.MessageFormat;
import java.util.List;

import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.dto.UpdatesQuery;
import de.oglimmer.lunchy.services.EmailUpdatesNotifier.MailImage;

public enum NotificationEmailText {
	INSTANCE;

	private static String CR = "\r\n";
	private static String BR = "<br/>";

	private static final String HEAD_STYLE = "a {color:black; } a.plain {text-decoration: none; } figure, figcaption {display: block; } #polaroid{width:100%; overflow:hidden; padding:20px 10px; } #polaroid figure{float:left; position:relative; width:480px; margin:10px 20px; padding: 6px 8px 10px 8px; -webkit-box-shadow: 4px 4px 8px -4px rgba(0, 0, 0, .75); -moz-box-shadow: 4px 4px 8px -4px rgba(0, 0, 0, .75); box-shadow: 4px 4px 8px -4px rgba(0, 0, 0, .75); background: #eee6d8; background: -webkit-linear-gradient(top, #ede1c9, #fef8e2 20%, #f2ebde 60%); background: -moz-linear-gradient(top, #ede1c9, #fef8e2 20%, #f2ebde 60%); background: -o-linear-gradient(top, #ede1c9, #fef8e2 20%, #f2ebde 60%); background: -ms-linear-gradient(top, #ede1c9, #fef8e2 20%, #f2ebde 60%); background: linear-gradient(top, #ede1c9, #fef8e2 20%, #f2ebde 60%); -webkit-transform:rotate(-1deg); -moz-transform: rotate(-1deg); -o-transform: rotate(-1deg); -ms-transform: rotate(-1deg); transform: rotate(-1deg); -webkit-backface-visibility:hidden; } #polaroid figure:nth-child(even) {-webkit-transform:rotate(2deg); -moz-transform: rotate(2deg); -o-transform: rotate(2deg); -ms-transform: rotate(2deg); transform: rotate(2deg); -webkit-backface-visibility:hidden; -webkit-box-shadow: 4px 4px 8px -4px rgba(0, 0, 0, .75); -moz-box-shadow: 4px 4px 8px -4px rgba(0, 0, 0, .75); box-shadow: -4px 4px 8px -4px rgba(0, 0, 0, .75); } #polaroid figure:before {content: ''; display: block; position: absolute; left:5px; top: -15px; width: 75px; height: 25px; background-color: rgba(222,220,198,0.7); -webkit-transform: rotate(-12deg); -moz-transform: rotate(-12deg); -o-transform: rotate(-12deg); -ms-transform: rotate(-12deg); } #polaroid figure:nth-child(even):before {left:150px; top: -15px; width: 55px; height: 25px; -webkit-transform: rotate(12deg); -moz-transform: rotate(12deg); -o-transform: rotate(12deg); -ms-transform: rotate(12deg); } #polaroid figcaption{text-align:center; font-family: cursive; font-size:1.3em; color:#454f40; letter-spacing:0.09em; } #polaroid figure{-pie-background: linear-gradient(#ede1c9, #fef8e2 20%, #f2ebde 60%); behavior: url(assets/pie/PIE.htc); position:relative; padding-top:10px\\9; padding-right:10px\\9; }";
	private static final String BODY_STYLE = "background: rgba(141,215,255,1);background: -moz-linear-gradient(top, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);background: -webkit-gradient(left top, left bottom, color-stop(0%, rgba(141,215,255,1)), color-stop(100%, rgba(175,204,220,1)));background: -webkit-linear-gradient(top, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);background: -o-linear-gradient(top, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);background: -ms-linear-gradient(top, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);background: linear-gradient(to bottom, rgba(141,215,255,1) 0%, rgba(175,204,220,1) 100%);filter: progid:DXImageTransform.Microsoft.gradient( startColorstr=\"#8dd7ff\", endColorstr=\"#afccdc\", GradientType=0 );font-family:\"Helvetica Neue\", Helvetica, Arial, sans-serif;font-size:18px";
	private static final String DIV_STYLE = "width:800px;margin-bottom:50px;padding:0px 20px 20px 20px;background-color:#f6f6ef;margin-left:auto;margin-right:auto;border:3px solid black;border-radius:8px;-webkit-box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);-moz-box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);box-shadow: 10px 10px 5px 0px rgba(0,0,0,0.75);";

	private static final String greeting = "Hello {0} my hungry friend,";
	private static final String top_intro1 = "This is the weekly update from {0}.";
	private static final String top_intro2 = "Here are {0} new pictures which have been added last week: ";
	private static final String middle_text = "Beside of these pictures, this happened last week at {0}:";
	private static final String end_text1 = "Check out new lunch places at {0} today or contribute with your own pictures and reviews.";
	private static final String end_text2 = "Btw, lunchy works on your mobile as well - you can even upload pictures from a mobile.";
	private static final String cheers = "Regards,";
	private static final String sender = "Oli";
	private static final String unsubscribe_line = "To unsubscribe from this email change your settings at {0}";
	private static final String no_updates = "Nothing happened this week :( - visit lunchy now and enter new locations, reviews and pictures!";

	public String getHtml(UsersRecord rec, List<UpdatesQuery> updates, List<MailImage> images) {
		StringBuilder body = new StringBuilder("<html><head><style>" + HEAD_STYLE + "</style></head><body style='" + BODY_STYLE + "'>");
		body.append("<div style='" + DIV_STYLE + "'>");
		body.append("<h2>" + MessageFormat.format(greeting, rec.getDisplayname()) + "</h2>").append(BR);
		if (updates.isEmpty()) {
			body.append(BR + no_updates + BR);
		} else {
			body.append(MessageFormat.format(top_intro1, getHtmlUrl(rec.getFkCommunity())));
			body.append(BR);
			body.append(BR);
			body.append(MessageFormat.format(top_intro2, images.size()));
			body.append(BR);
			body.append("<div id='polaroid'>");
			for (MailImage mi : images) {
				body.append("<figure>");
				body.append("<a class='plain' href='" + Email.INSTANCE.getUrl(rec.getFkCommunity()) + "/#/view/" + mi.getId() + "'>");
				addImage(body, mi, rec.getFkCommunity());
				if (mi.getCaption() != null && !mi.getCaption().isEmpty()) {
					body.append("<figcaption>" + mi.getDisplayname() + ": " + mi.getCaption() + "</figcaption>");
				} else {
					body.append("<figcaption>" + mi.getDisplayname() + " mades this at " + mi.getOfficialName() + "</figcaption>");
				}
				body.append("</a>");
				body.append("</figure>");
			}
			body.append("</div>");
			body.append(MessageFormat.format(middle_text, getHtmlUrl(rec.getFkCommunity())));
			body.append(BR);
			body.append("<ul>");
			for (UpdatesQuery up : updates) {
				body.append("<li><a class='plain' href='" + Email.INSTANCE.getUrl(rec.getFkCommunity()) + "/#/" + up.getRef() + "'>")
						.append(up.getText()).append("</a></li>");
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

	private Object getHtmlUrl(int fkCommunity) {
		String link = Email.INSTANCE.getUrl(fkCommunity);
		String domain = Email.INSTANCE.getDomain(fkCommunity);
		return "<a href='" + link + "'>" + domain + "</a>";
	}

	public String getText(UsersRecord rec, List<UpdatesQuery> updates, List<MailImage> images) {
		StringBuilder body = new StringBuilder();
		body.append(MessageFormat.format(greeting, rec.getDisplayname())).append(CR);
		body.append(CR);
		if (updates.isEmpty()) {
			body.append(CR + no_updates + CR);
		} else {
			body.append(MessageFormat.format(top_intro1, Email.INSTANCE.getUrl(rec.getFkCommunity())));
			body.append(CR);
			body.append(MessageFormat.format(top_intro2, images.size()));
			body.append(CR);
			body.append(CR);
			body.append(MessageFormat.format(middle_text, Email.INSTANCE.getUrl(rec.getFkCommunity())));
			body.append(CR);
			body.append(CR);
			for (UpdatesQuery up : updates) {
				body.append("* ").append(up.getText());
			}
		}
		body.append(CR);
		body.append(MessageFormat.format(end_text1, Email.INSTANCE.getUrl(rec.getFkCommunity())));
		body.append(CR);
		body.append(CR);
		body.append(end_text2);
		body.append(CR);
		body.append(CR);
		body.append(cheers).append(CR);
		body.append(sender).append(CR);
		body.append(CR);
		body.append(CR);
		body.append(MessageFormat.format(unsubscribe_line, Email.INSTANCE.getUrl(rec.getFkCommunity())));
		return body.toString();
	}

	private void addImage(StringBuilder body, MailImage mi, int fkCommunity) {
		body.append("<img src=\"" + getBasePicUrl(fkCommunity) + mi.getFilename() + "?size=small\" />");
	}

	private String getBasePicUrl(int fkCommunity) {
		return Email.INSTANCE.getUrl(fkCommunity) + "/rest/pictures/";
	}
}
