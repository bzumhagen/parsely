# parsely
A system for parsing and sorting records

## Usage
### Console
Parsely can be used as a standalone file parser with the following command `sbt run -f <path to input file>`. After parsing the file, it will output the data in a tabular format sorted by different parameters.

### Server
Parsely can be used as a web server with the following command `sbt run`.
Currently supported routes are

POST `/records` - Add a single record to the server

GET `/records/gender` - Get records sorted by gender (asc) + last name (asc)

GET `/records/birthdate` - Get records sorted by birth date (asc)

GET `/records/name` - Get records sorted by last name (desc)

### Data Format
Supported input delimiters are `|` `,` ` `.

Input is expected to have the following fields in the order given: Last Name, First Name, Gender, Favorite Color, Date of Birth (MM/dd/yyyy format).

i.e. `Doe | Jane | Female | Blue | 01/22/1990`

### Testing
Tests can be executed with the following command `sbt test`.

Test coverage can be calculated with the following command `sbt clean coverage test coverageReport`

### Docker
Docker image can be generated with the following command `sbt docker`

### Changelog
Changelog can be generated with the following command `sbt changelog`