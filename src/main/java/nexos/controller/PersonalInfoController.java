package nexos.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.MongoClients;

import nexos.exception.ErrorDetails;
import nexos.exception.ResourceNotFoundException;
import nexos.model.Person;
import nexos.model.Personalinfo;
import nexos.repository.LogRepository;
import nexos.repository.PersonRepository;
import nexos.repository.PersonalInfoRepository;
import nexos.service.SequenceGeneratorService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class PersonalInfoController {
	
	private static final Logger logger = LogManager.getLogger(PersonalInfoController.class);

	@Autowired private MongoOperations mongoOps;
	
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PersonalInfoRepository personalinfoRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	//implementacion para generar el Log Generico cada cierto tiempo enviar log de peticiones a la base de datos
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	
	private ErrorDetails save;

	//MEtodo get para llamar todos registros de personas
	@GetMapping("/person")
	public List<Person> getAllPersonal() {
		List<Person> personas = null;
		try {
			// Funcion Generica para realizar consulta  db.person.find()
			logger.info("Consulta de personas");
			LogBD("200", "getAllPersonal Exitoso"); 
			personas = personRepository.findAll();
			
		} catch (Exception e) {
			
			LogBD("500", "getAllPersonal Falló"); 
		}
		return personas;
	}

	/*
	 * Se realiza consumo para traer listado completo de personas y familiares
	 * */
	@GetMapping("/personalinfo")
	public List<Personalinfo> getAllPersonalInfo() {
		List<Personalinfo> personas = null;
		try {
			// Funcion Generica para realizar consulta  db.person.find()
			logger.info("Consulta de personas");
			LogBD("200", "getAllPersonal Exitoso"); 
			personas = personalinfoRepository.findAll();
			
		} catch (Exception e) {
			
			LogBD("500", "getAllPersonal Falló");
		}
		return personas;
	}
	
	/*
	 * Se realiza consumo para traer personas por Id de personas y familiares
	 * */
	@GetMapping("/person/{id}")
	public ResponseEntity<Person> getPersonById(@PathVariable(value = "id") Long personalId)
			throws ResourceNotFoundException {
		ResponseEntity<Person> peronsa = null;
		try {
			// Funcion Generica para realizar consulta  db.person.find()
			logger.info("Consulta de personas");
			LogBD("200", "getAllPersonal Exitoso"); 
			Person person = personRepository.findById(personalId)
					.orElseThrow(() -> new ResourceNotFoundException("No se encuentra persona :: " + personalId));
			
			peronsa=  ResponseEntity.ok().body(person);
		} catch (Exception e) {
			logger.info("Consulta de personas");
			LogBD("500", "error al traer la informacion");
		} 
		return peronsa;

	}
	

	@PostMapping("/person")
	public Person createPerson(@Valid @RequestBody Person person) {
		// Funcion para realizar agregado de una persona unica puede ser padre hijo o familiar
		Person personal = new Person() ; 
		try {
			person.setId(sequenceGeneratorService.generateSequence(Person.SEQUENCE_NAME));
			personal= personRepository.save(person);
		} catch (Exception e) {
			logger.info("Consulta de personas");
			LogBD("500", "error al traer la informacion");
		} 
		return personal;
	}
	
	@PostMapping("/personalinfo")
	public Personalinfo createPersonalInfo(@Valid @RequestBody Personalinfo personalInfo) throws InterruptedException, ExecutionException {
		
		Personalinfo personal = new Personalinfo();
		try {
			//llamado de metodo para cargar un log azincrono en la base de datos
			Boolean resullogazinc =false;
			Future<Boolean> future = LogAsincrono();
			//Mientras que el hijo no se finalice mostrara un mensaje en consola y log indicandoq ue esta cargando el log
			while(!future.isDone()) {
			    System.out.println("Geerando Log Azincrono a la Base de datos");
			    logger.info("Geerando Log Azincrono a la Base de datos");
			    Thread.sleep(300);
			}
			 
			resullogazinc = future.get();
			
			
			personalInfo.setId(sequenceGeneratorService.generateSequence(Personalinfo.SEQUENCE_NAME));
			personalInfo.getPersonal().setId(sequenceGeneratorService.generateSequence(Person.SEQUENCE_NAME));
			personRepository.save(personalInfo.getPersonal());
			for (Person p:personalInfo.getFamiliares()) {
				p.setId(sequenceGeneratorService.generateSequence(Person.SEQUENCE_NAME));
				personRepository.save(p);
			}
			personal= personalinfoRepository.save(personalInfo);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return personal;
	}

	@PutMapping("/person/{id}")
	public ResponseEntity<Person> updateperson(@PathVariable(value = "id") Long personId,
			@Valid @RequestBody Person person) throws ResourceNotFoundException {
		
		ResponseEntity<Person> personal = null;
		try {
			Person personn = personRepository.findById(personId).orElseThrow(() -> new ResourceNotFoundException("No se encuentra id de persona :: " + personId));

			personn.setDocIdentidad(person.getDocIdentidad());
			personn.setNombre(person.getNombre());
			personn.setApellido(person.getApellido());
			personn.setTelefono(person.getTelefono());
			personn.setTipo(person.getTipo());
			final Person updateperson = personRepository.save(personn);
			personal= ResponseEntity.ok(updateperson);
			logger.info("Consulta de personas");
			LogBD("200", "getAllPersonal Exitoso"); 
		} catch (Exception e) {
			logger.info("Consulta de personas");
			LogBD("500", "error al traer la informacion");
		}
		return personal;
	}
	
	@PutMapping("/personalInfo/{id}")
	public ResponseEntity<Personalinfo> updatepersonalInfo(@PathVariable(value = "id") Long personalInfoId,
			@Valid @RequestBody Personalinfo personalinfo) throws ResourceNotFoundException, InterruptedException, ExecutionException {

		
		try {
			//llamado de metodo para cargar un log azincrono en la base de datos
			Boolean resullogazinc =false;
			Future<Boolean> future = LogAsincrono();
			//Mientras que el hijo no se finalice mostrara un mensaje en consola y log indicandoq ue esta cargando el log
			while(!future.isDone()) {
			    System.out.println("Geerando Log Azincrono a la Base de datos");
			    logger.info("Geerando Log Azincrono a la Base de datos");
			    Thread.sleep(300);
			}
			 
			resullogazinc = future.get();
			
			Personalinfo personalinfof = personalinfoRepository.findById(personalInfoId).orElseThrow(() -> new ResourceNotFoundException("No se encuentra id de persona :: " + personalInfoId));
			
			personalinfof.getPersonal().setDocIdentidad(personalinfo.getPersonal().getDocIdentidad());
			personalinfof.getPersonal().setNombre(personalinfo.getPersonal().getNombre());
			personalinfof.getPersonal().setApellido(personalinfo.getPersonal().getApellido());
			personalinfof.getPersonal().setTelefono(personalinfo.getPersonal().getTelefono());
			personalinfof.getPersonal().setTipo(personalinfo.getPersonal().getTipo());	
			
			Person personn = personRepository.findById(personalinfo.getPersonal().getId()).orElseThrow(() -> new ResourceNotFoundException("No se encuentra id de persona :: " + personalinfo.getPersonal().getId()));
			
			personn.setDocIdentidad(personalinfo.getPersonal().getDocIdentidad());
			personn.setNombre(personalinfo.getPersonal().getNombre());
			personn.setApellido(personalinfo.getPersonal().getApellido());
			personn.setTelefono(personalinfo.getPersonal().getTelefono());
			personn.setTipo(personalinfo.getPersonal().getTipo());
			personRepository.save(personn);
			
			ArrayList<Person> familia = new ArrayList<>();
			for (Person p:personalinfof.getFamiliares()) {
				Person personal = personRepository.findById(p.getId()).orElseThrow(() -> new ResourceNotFoundException("No se encuentra id de persona :: " + personalinfo.getPersonal().getId()));
				personal.setDocIdentidad(p.getDocIdentidad());
				personal.setNombre(p.getNombre());
				personal.setApellido(p.getApellido());
				personal.setTelefono(p.getTelefono());
				personal.setTipo(p.getTipo());
				
				familia.add(personal);
				
				Person personnn = personRepository.findById(p.getId()).orElseThrow(() -> new ResourceNotFoundException("No se encuentra id de persona :: " + personalinfo.getPersonal().getId()));
				personnn.setDocIdentidad(p.getDocIdentidad());
				personnn.setNombre(p.getNombre());
				personnn.setApellido(p.getApellido());
				personnn.setTelefono(p.getTelefono());
				personnn.setTipo(p.getTipo());
				personRepository.save(personnn);
			}
			personalinfof.setFamiliares(familia);
			final Personalinfo personalinfoo = personalinfoRepository.save(personalinfof);
			
			return ResponseEntity.ok(personalinfoo);
		} finally {
			logger.info("Consulta de personas");
			LogBD("500", "error al traer la informacion");
		}
		
	}

	@DeleteMapping("/personalInfo/{id}")
	public Map<String, Boolean> deletePersonal(@PathVariable(value = "id") Long personalId)
			throws ResourceNotFoundException {
		Personalinfo personalinfo = personalinfoRepository.findById(personalId)
				.orElseThrow(() -> new ResourceNotFoundException("no se encuentra personal con id : " + personalId));
		Person personn = personRepository.findById(personalinfo.getPersonal().getId())
				.orElseThrow(() -> new ResourceNotFoundException("No se encuentra id de persona :: " + personalinfo.getPersonal().getId()));
		personalinfoRepository.delete(personalinfo);
		personRepository.delete(personn);		
		for (Person p:personalinfo.getFamiliares()) {
			Person personfamily = personRepository.findById(p.getId())
					.orElseThrow(() -> new ResourceNotFoundException("No se encuentra id de persona :: " + p.getId()));
			personRepository.delete(personfamily);
		}
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
	public void LogBD(String message, String details) {
		Date fecha = new Date();
		
		ErrorDetails errordetail = new ErrorDetails();
		errordetail.setTimestamp(fecha);
		errordetail.setDetails(details);
		errordetail.setMessage(message);
	    mongoOps.insert(errordetail);
	} 
	    
    public Future<Boolean> LogAsincrono() {        
        return executor.submit(() -> {
        	Thread.sleep(10000);
        	LogBD("Azyc log", "Log Asincrono generado correctamente");
            return true;
        });
    }
    

}
