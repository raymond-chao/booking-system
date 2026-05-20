package com.raymond.bookingsystem.seeder;


import com.raymond.bookingsystem.model.Room;
import com.raymond.bookingsystem.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoomSeeder implements CommandLineRunner {


        private final RoomRepository roomRepository;

        public RoomSeeder(RoomRepository roomRepository) {
            this.roomRepository = roomRepository;
        }

        @Override
        public void run(String... args) {
            if (roomRepository.count() == 0) {
                roomRepository.save(new Room("101" , 1,800));
                roomRepository.save(new Room("102" , 1,800));
                roomRepository.save(new Room("103" , 1,800));
                roomRepository.save(new Room("104" , 1,800));
                roomRepository.save(new Room("105" , 2,1100));
                roomRepository.save(new Room("106" , 2,1100));
                roomRepository.save(new Room("107" , 2,1100));
                roomRepository.save(new Room("108" , 3,2100));
                roomRepository.save(new Room("109" , 3,2100));
                roomRepository.save(new Room("110" , 3,2100));


            }
        }
}
