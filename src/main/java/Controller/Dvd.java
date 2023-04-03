package Controller;

import Model.Genre;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name="dvds")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dvd {
    @Id
    // AUTO INCREMENT ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id;
    //UNIQUE TITLE
    @Column(name = "title", unique = true)
    private String title;
    @Column(name="genre")
    //Genre out of a pool of specific values 
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @Column(name="available")
    //Available pieces
    private int available;
    
    
}
