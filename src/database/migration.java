import com.github.cloudyrock.mongock.ChangeLog;

@ChangeLog
public class migration {

    @ChangeSet(order = "001", id="seedDatabase", author="Sai")
    public void seedDatabase(){

    }
}
