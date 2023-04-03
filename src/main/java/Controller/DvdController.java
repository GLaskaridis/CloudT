package Controller;


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
    DvdController(){}
    
    @PostMapping("/dvd/add")
    //JSON
    public String addDvd(@RequestBody Dvd dvd){
        String input = dvd.getTitle();
        List temp = dvdrepo.findByTitle(input);
        
        if(temp.isEmpty()){
            dvdrepo.save(dvd);
        return "Created";
        }
        else
            return "Already exists";
    }

 @GetMapping("/dvds")
public ResponseEntity<?> searchDvd(@RequestParam(required = false) String search) {
    if (search == null || search.equals("all")) {
        List<Dvd> dvdList = dvdrepo.findAll();
        return new ResponseEntity<>(dvdList, HttpStatus.OK);
    } else if (search.matches("\\d+")) {
        long id = Long.parseLong(search);
        Optional<Dvd> dvd = dvdrepo.findById(id);
        if (dvd.isPresent()) {
            return new ResponseEntity<>(dvd.get(), HttpStatus.OK);
        }
    } else {
        List<Dvd> dvdList = dvdrepo.findByTitle(search);
        if (!dvdList.isEmpty()) {
            return new ResponseEntity<>(dvdList, HttpStatus.OK);
        }
    }
    List<Dvd> dvdList = dvdrepo.findAll();
    return new ResponseEntity<>(dvdList, HttpStatus.OK);
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
//JSON
public ResponseEntity<Dvd> updateDvdGenre(@PathVariable("key") Long id, @RequestBody GenreClass genre) {
    
    Optional<Dvd> dvdOptional = dvdrepo.findById(id);
    if (!dvdOptional.isPresent()) {
        return ResponseEntity.notFound().build();
    }
    Dvd dvd = dvdOptional.get();
    dvd.setGenre(Genre.valueOf(genre.getGenre()));
    dvdrepo.save(dvd);
    return ResponseEntity.ok(dvd);
}


   @PutMapping("/dvd/update/available/{id}")
public ResponseEntity<?> updateDvdAvailable(@PathVariable("id") long id, @RequestParam("available") int available) {
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
