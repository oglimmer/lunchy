package de.oglimmer.lunchy.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import lombok.AllArgsConstructor;
import de.oglimmer.lunchy.database.dao.CommunityDao;
import de.oglimmer.lunchy.database.dao.OfficeDao;
import de.oglimmer.lunchy.database.dao.UserDao;
import de.oglimmer.lunchy.database.generated.tables.records.CommunitiesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.OfficesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersRecord;
import de.oglimmer.lunchy.rest.UserProvider;
import de.oglimmer.lunchy.rest.dto.CommunityCreateInput;
import de.oglimmer.lunchy.rest.dto.ResultParam;

@Path("communities")
public class CommunityResource extends BaseResource {

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{domain}")
	public ResultParam checkByDomainName(@Context HttpServletRequest request, @PathParam("domain") String domain) {
		CommunitiesRecord community = CommunityDao.INSTANCE.getByDomainNoCache(domain);
		ResultParam rp = new ResultParam();
		if (community != null) {
			rp.setSuccess(true);
			rp.setErrorMsg(Integer.toString(community.getId()));
		}
		return rp;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void create(CommunityCreateInput communityDto) {
		new CreateUpdateLogic(communityDto).create();
	}

	@AllArgsConstructor
	class CreateUpdateLogic {

		private CommunityCreateInput communityDto;

		public void create() {
			CommunitiesRecord rec = communityDto.toCommunity();
			updateRec(rec);
			OfficesRecord office = communityDto.toOffice();
			addInitialData(office, rec);
			updateRec(office);
			UsersRecord user = communityDto.toUser();
			addInitialData(user, office, rec);
			UserProvider.INSTANCE.makeNew(user, communityDto.getPassword());
			user.setPermissions(2);
			UserProvider.INSTANCE.setEmailUpdates(user, user.getEmailUpdates());
			updateRec(user);
			UserProvider.INSTANCE.sendEmail(user);
		}

		private void addInitialData(OfficesRecord office, CommunitiesRecord community) {
			office.setFkCommunity(community.getId());
			if (office.getCountry() == null) {
				office.setCountry("");
			}
		}

		private void addInitialData(UsersRecord users, OfficesRecord office, CommunitiesRecord community) {
			users.setFkCommunity(community.getId());
			users.setFkBaseOffice(office.getId());
		}

		private void updateRec(CommunitiesRecord rec) {
			CommunityDao.INSTANCE.store(rec);
		}

		private void updateRec(OfficesRecord rec) {
			OfficeDao.INSTANCE.store(rec);
		}

		private void updateRec(UsersRecord rec) {
			UserDao.INSTANCE.store(rec);
		}

	}
}
