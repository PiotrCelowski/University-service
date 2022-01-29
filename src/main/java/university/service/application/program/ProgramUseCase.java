package university.service.application.program;

import org.springframework.stereotype.Component;
import university.service.domain.program.ProgramEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProgramUseCase {

    public ProgramEntity createProgram(String name, String description) {
        return new ProgramEntity(name, description);
    }

    public List<ProgramEntity> createDummyPrograms() {
        ArrayList<ProgramEntity> dummyPrograms = new ArrayList<>();

        dummyPrograms.add(new ProgramEntity("Maths", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent vel urna ut tellus efficitur efficitur non id lacus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean ipsum ex, consequat et porttitor vitae, auctor eu quam. Quisque at neque sit amet mi dapibus elementum quis vel diam. Proin tincidunt lobortis dolor, at malesuada dui condimentum sed. Quisque blandit dui a neque pulvinar, luctus fermentum ex pharetra. In lorem libero, imperdiet ac fringilla ut, scelerisque et massa. Phasellus mollis lectus at maximus euismod. Phasellus condimentum, nisl ac varius tempus, diam elit convallis nisl, ut blandit est ex ac metus. Aliquam erat volutpat. Integer finibus auctor viverra. Curabitur sodales sodales cursus. Donec ut rhoncus nisi."));
        dummyPrograms.add(new ProgramEntity("Polish", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent vel urna ut tellus efficitur efficitur non id lacus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean ipsum ex, consequat et porttitor vitae, auctor eu quam. Quisque at neque sit amet mi dapibus elementum quis vel diam. Proin tincidunt lobortis dolor, at malesuada dui condimentum sed. Quisque blandit dui a neque pulvinar, luctus fermentum ex pharetra. In lorem libero, imperdiet ac fringilla ut, scelerisque et massa. Phasellus mollis lectus at maximus euismod. Phasellus condimentum, nisl ac varius tempus, diam elit convallis nisl, ut blandit est ex ac metus. Aliquam erat volutpat. Integer finibus auctor viverra. Curabitur sodales sodales cursus. Donec ut rhoncus nisi."));
        dummyPrograms.add(new ProgramEntity("Physics", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent vel urna ut tellus efficitur efficitur non id lacus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Aenean ipsum ex, consequat et porttitor vitae, auctor eu quam. Quisque at neque sit amet mi dapibus elementum quis vel diam. Proin tincidunt lobortis dolor, at malesuada dui condimentum sed. Quisque blandit dui a neque pulvinar, luctus fermentum ex pharetra. In lorem libero, imperdiet ac fringilla ut, scelerisque et massa. Phasellus mollis lectus at maximus euismod. Phasellus condimentum, nisl ac varius tempus, diam elit convallis nisl, ut blandit est ex ac metus. Aliquam erat volutpat. Integer finibus auctor viverra. Curabitur sodales sodales cursus. Donec ut rhoncus nisi."));

        return dummyPrograms;
    }

    public List<ProgramEntity> getAllPrograms() {
        return createDummyPrograms();
    }
}
