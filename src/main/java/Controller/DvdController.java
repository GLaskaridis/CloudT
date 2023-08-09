package Controller;

import Controller.Dvd;
import Controller.DvdRepository;
import Controller.GenreClass;
import Model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DvdController {

    @Autowired
    DvdRepository dvdrepo;

    DvdController() {
    }

    @PostMapping("/dvd/add")
    //JSON
    public ResponseEntity<?> addDvd(@RequestBody Dvd dvd) {
        String input = dvd.getTitle();
        List temp = dvdrepo.findByTitle(input);

        //elegxos gia titlo
        if (dvd.getTitle().isEmpty()) {
            return new ResponseEntity<>("DVD should have a title", HttpStatus.BAD_REQUEST);
        }

        //elegxos temaxiwn dvd
        if (dvd.getAvailable() <= 0) {
            return new ResponseEntity<>("Wrong DVD items!", HttpStatus.BAD_REQUEST);
        }

        //elegxos swstou genre gia dvd
        try {
            Genre genre = Genre.valueOf(dvd.getGenre().toString());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("DVD genre not exists!", HttpStatus.BAD_REQUEST);
        }

        if (temp.isEmpty()) {
            dvdrepo.save(dvd);
            return new ResponseEntity<>("DVD Created!", HttpStatus.OK);
        } else //ean yparxei idi o titlos
        {
            return new ResponseEntity<>("Title of dvd already exists", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> searchTitle(String search) {
        List<Dvd> dvdList = dvdrepo.findByTitle(search);
        if (!dvdList.isEmpty()) {
            return new ResponseEntity<>(dvdList, HttpStatus.OK);
        }
        else return new ResponseEntity<>("Title or ID Not Found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> searchID(String search) {
        long id = Long.parseLong(search);
        Optional<Dvd> dvd = dvdrepo.findById(id);
        if (dvd.isPresent()) {
            return new ResponseEntity<>(dvd.get(), HttpStatus.OK);
        }
        else return new ResponseEntity<>("Title or Id Not Found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dvds")
    public ResponseEntity<?> searchDvd(@RequestParam(required = false) String search) {
        if (search == null || search.equals("all")) {
            List<Dvd> dvdList = dvdrepo.findAll();
            return new ResponseEntity<>(dvdList, HttpStatus.OK);
        } else if (search.matches("\\d+")) {
            return searchID(search);

        } else if (search.matches("[a-zA-Z0-9\\s]+")) {
            return searchTitle(search);
        }
        else {
           return new ResponseEntity<>("Bad Request ", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/dvd/delete/{key}")
    public ResponseEntity<?> deleteDvd(@PathVariable("key") long key) {
        Optional<Dvd> dvd = dvdrepo.findById(key);
        if (dvd.isPresent()) {
            dvdrepo.deleteById(key);
            return new ResponseEntity<>("Dvd with id " + key + " has been deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Dvd with id " + key + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/dvd/updateGenre/{key}")
    public ResponseEntity<?> updateDvdGenre(@PathVariable("key") Long id, @RequestBody GenreClass genre) {

        if (id == null)
            return new ResponseEntity<>("Dvd with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        
        Optional<Dvd> dvdOptional = dvdrepo.findById(id);
        if (!dvdOptional.isPresent()) {
            return new ResponseEntity<>("Dvd with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
        Dvd dvd = dvdOptional.get();
        Genre newgenre;
        //elegxos swstou genre gia dvd
        try {
            newgenre = Genre.valueOf(dvd.getGenre().toString());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("DVD genre not exists!", HttpStatus.BAD_REQUEST);
        }
        
        dvd.setGenre(newgenre);
        dvdrepo.save(dvd);
        return new ResponseEntity<>("Dvd with id " + id + " has been deleted", HttpStatus.OK);
    }

    @PutMapping("/dvd/update/available/{id}")
    public ResponseEntity<?> updateDvdAvailable(@PathVariable("id") Long id, @RequestParam("available") int available) {
        if (id == null)
            return new ResponseEntity<>("Wrong id", HttpStatus.NOT_FOUND);
        
         //elegxos temaxiwn dvd
        if (available <= 0) {
            return new ResponseEntity<>("Wrong DVD items!", HttpStatus.BAD_REQUEST);
        }
        
        Optional<Dvd> dvd = dvdrepo.findById(id);
        
        if (dvd.isPresent()) {
            dvd.get().setAvailable(available);
            dvdrepo.save(dvd.get());
            return new ResponseEntity<>("Dvd available quantity updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Dvd with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

}