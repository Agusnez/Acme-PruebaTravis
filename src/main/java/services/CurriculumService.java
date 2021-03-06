
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CurriculumRepository;
import security.Authority;
import domain.Actor;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.ProfessionalRecord;

@Service
@Transactional
public class CurriculumService {

	//Managed repository---------------------------------
	@Autowired
	private CurriculumRepository		curriculumRepository;

	//Suporting services---------------------------------
	@Autowired
	private PersonalRecordService		personalRecordService;

	@Autowired
	private EducationRecordService		educationRecordService;

	@Autowired
	private ProfessionalRecordService	professionalRecordService;

	@Autowired
	private EndorserRecordService		endorserRecordService;

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private ActorService				actorService;


	//Simple CRUD methods--------------------------------
	public Curriculum create() {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		final Authority authority = new Authority();
		authority.setAuthority(Authority.HANDYWORKER);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));

		Curriculum c;
		c = new Curriculum();

		final PersonalRecord pr = this.personalRecordService.create();

		final Collection<EducationRecord> c1 = new HashSet<>();

		final Collection<ProfessionalRecord> c2 = new HashSet<>();

		final Collection<EndorserRecord> c3 = new HashSet<>();

		final Collection<MiscellaneousRecord> c4 = new HashSet<>();

		c.setEducationRecords(c1);
		c.setEndorserRecords(c3);
		c.setMiscellaneousRecords(c4);
		c.setPersonalRecord(pr);
		c.setProfessionalRecords(c2);

		return c;
	}

	public Collection<Curriculum> findAll() {
		Collection<Curriculum> result;
		result = this.curriculumRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Curriculum findOne(final int curriculumId) {
		Curriculum c;
		c = this.curriculumRepository.findOne(curriculumId);
		Assert.notNull(c);
		return c;
	}

	public Curriculum save(final Curriculum curriculum) {
		Assert.notNull(curriculum);

		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);

		final Actor owner = curriculum.getHandyWorker();

		Assert.isTrue(actor.getId() == owner.getId());

		Curriculum c;
		c = this.curriculumRepository.save(curriculum);
		return c;
	}

	//Other business methods----------------------------

	public Curriculum findByHandyWorkerId(final int handyWorkerId) {

		final Curriculum result = this.curriculumRepository.findByHandyWorkerId(handyWorkerId);

		return result;
	}

}
