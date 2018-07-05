/*test data used for JUnit tests*/

Use hsbcbooking;

DELETE FROM deskreservation WHERE employee = '12345678' OR employee = '11112222' OR employee = '43868488';

DELETE FROM location WHERE officename = 'Richmond Westminster Red';

DELETE FROM desk where location = 'Richmond Westminster Red';

DELETE FROM archive WHERE emp_id = '11112222' OR emp_id = '12345678' OR emp_id = '43868488';

DELETE FROM admin where adminid = '43868488' OR adminid = '12345678' OR adminid = '11112222';

DELETE FROM employee where id = '11112222' OR id = '12345678' or id = '87654321';