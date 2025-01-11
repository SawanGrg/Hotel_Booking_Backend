Database Name:
-table name:

            BaseEntity
- table columns:

            UUID id
            LocalDateTime created_at
            LocalDateTime updated_at

-table name:

            role extends BaseEntity
- table columns:

            String roleName
            Set<Users> users = new HashSet<>(); // many to many relationship

- table name:

            user extends BaseEntity
- cardinality:
            many to many relationship
                                   many user can have many role
            one to many relationship
                                   one user can have many reviews
                                   one user can have many houses
                                   one user can have many rooms 
                                   one user can have many flats

table columns:

              String firstName
              String lastName
              String userName
              String email
              String password
              String phoneNumber
              LocalDate dob
              Boolean isVerified
              String citizenshipFront  for owner
              String citizenshipBack   for owner
              Enum { male, female, other } gender 
              Enum { employee, student, other } userStatus
              Enum { single, married, bachelor, other } maritalStatus 
              Set<Role> roles = new HashSet<>(); // many to many relationship

- table name:

            area extends BaseEntity

- cardinality:
            one to one relationship
                                    one area can have one house
                                    one area can have one room
                                    one area can have one flat
- table columns:

            Long length
            Long breadth
            Long area

- table name:

            review extends BaseEntity
-  cardinality:
            many to one relationship
                                    many reviews can have one house
                                    many reviews can have one room
                                    many reviews can have one flat
- table columns:

            String review
            User reviewerID
            House houseID
            Room roomID
            Flat flatID
            Boolean isApproved
            LocalDateTime reviewDate

- table name:

            assets extends BaseEntity

- cardinality:
             many to one relationship
                                    many assets can have one house
                                    many assets can have one room
                                    many assets can have one flat
- table columns:

            String assetURL
            String assetType --> image, video
            House houseID
            Room roomID
            Flat flatID
            LocalDateTime createdDate
            LocalDateTime updatedDate

- table name:

            Amenities extends BaseEntity
- table columns:

            Boolean wifi
            Boolean parking
            Boolean Balcony
            Boolean CCTV
            Boolean water
            Boolean AC
            Boolean TV
            Boolean Fridge
            Boolean Fan
            Boolean securityStaff
            Boolean underGroundWaterTank
            Boolean SolarWaterHeater
            Enum { furnished, semiFurnished, unfurnished } furnishingStatus
            Set<Assets> assets = new HashSet<>(); // one to many relationship

- table name:
            Room extends BaseEntity

- cardinality:
            one to many relationship
                                    one room can have many reviews
                                    one room can have many assets
            one to one relationship
                                    one room can have one area
- table columns:
            String roomPrice
            String roomDescription

            String district
            String city
            String ward
            String tole

            Text roomRules

            LocalDateTime availableFrom
            LocalDateTime availableTo

            Boolean isAvailable
            Boolean isVerified

            Set<Assets> assets = new HashSet<>(); // one to many relationship

- table name:
            Flat extends BaseEntity

- cardinality:
            one to many relationship
                                    one flat can have many reviews
                                    one flat can have many assets
            one to one relationship
                                    one flat can have one area
- table columns:

            String flatPrice
            Text flatDescription

            String district
            String city
            String ward
            String tole

            String flatRules

            Integer bedRooms
            Integer bathRooms
            Integer kitchen
            Integer livingRoom

            LocalDateTime availableFrom
            LocalDateTime availableTo

            Boolean isAvailable
            Boolean isVerified

            Set<Assets> assets = new HashSet<>(); // one to many relationship

- table name:
            House extends BaseEntity

- cardinality:
            one to many relationship
                                    one house can have many reviews
                                    one house can have many assets
            one to one relationship
                                    one house can have one area

- table columns:
            String housePrice
            Text houseDescription

            String district
            String city
            String ward
            String tole

            String houseRules

            Integer houseFloors
            Integer bedRooms
            Integer bathRooms
            Integer kitchen
            Integer livingRoom
            
            LocalDateTime availableFrom
            LocalDateTime availableTo

            Boolean isAvailable
            Boolean isVerified

            Set<Assets> assets = new HashSet<>(); // one to many relationship


            
            

    
            