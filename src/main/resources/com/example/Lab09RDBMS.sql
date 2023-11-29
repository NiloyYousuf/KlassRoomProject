-- Table creation and data insertion
CREATE TABLE Members (
    member_id INT PRIMARY KEY,
    first_name VARCHAR2(20),
    last_name VARCHAR2(20),
    email VARCHAR2(50),
    phone VARCHAR2(20),
    loyalty_points INT
);

INSERT INTO Members VALUES
(1, 'Niloy', 'Chowdhury', 'niloy@email.com', '1234567890', 50);
INSERT INTO Members VALUES
(2, 'Asif', 'Ahmed', 'asif@email.com', '9876543210', 30);
INSERT INTO Members VALUES
(3, 'Samnun', 'Khan', 'samnun@email.com', '5551234567', 20);

CREATE TABLE Employees (
    employee_id INT PRIMARY KEY,
    first_name VARCHAR2(20),
    last_name VARCHAR2(20),
    job_title VARCHAR2(30)
);

INSERT INTO Employees VALUES
(1, 'Dipto', 'Yousuf', 'Fitness Trainer');
INSERT INTO Employees VALUES
(2, 'Arif', 'Islam', 'Receptionist');
INSERT INTO Employees VALUES
(3, 'Fahim', 'Hossain', 'Yoga Instructor');

CREATE TABLE Facilities (
    facility_id INT PRIMARY KEY,
    name VARCHAR2(30),
    location VARCHAR2(100),
    capacity INT,
    rate_per_hour NUMBER(10, 2)
);

INSERT INTO Facilities VALUES
(1, 'Cardio Area', 'Main Floor', 50, 5.00);
INSERT INTO Facilities VALUES
(2, 'Weightlifting Zone', 'Basement', 30, 7.50);
INSERT INTO Facilities VALUES
(3, 'Yoga Studio', '2nd Floor', 20, 10.00);

CREATE TABLE Classes (
    class_id INT PRIMARY KEY,
    name VARCHAR2(30),
    instructor_id INT,
    facility_id INT,
    schedule VARCHAR2(50),
    FOREIGN KEY (instructor_id) REFERENCES Employees(employee_id),
    FOREIGN KEY (facility_id) REFERENCES Facilities(facility_id)
);

INSERT INTO Classes VALUES
(1, 'Cardio Blast', 1, 1, 'Mon, Wed, Fri 6:00 PM - 7:00 PM');
INSERT INTO Classes VALUES
(2, 'Powerlifting ', 2, 2, 'Tue, Thu 5:00 PM - 6:30 PM');
INSERT INTO Classes VALUES
(3, 'Yoga Flow', 3, 3, 'Sat, Sun 9:00 AM - 10:30 AM');

CREATE TABLE Bookings (
    booking_id INT PRIMARY KEY,
    member_id INT,
    class_id INT,
    booking_date DATE,
    fees_paid NUMBER(10, 2),
    FOREIGN KEY (member_id) REFERENCES Members(member_id),
    FOREIGN KEY (class_id) REFERENCES Classes(class_id)
);

INSERT INTO Bookings VALUES
(1, 1, 1, TO_DATE('2023-11-14', 'YYYY-MM-DD'), 10.00);
INSERT INTO Bookings VALUES
(2, 2, 2, TO_DATE('2023-11-15', 'YYYY-MM-DD'), 15.00);
INSERT INTO Bookings VALUES
(3, 3, 3, TO_DATE('2023-11-16', 'YYYY-MM-DD'), 20.00);



-- Triggers
CREATE SEQUENCE employee_sequence
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE SEQUENCE member_sequence
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER employees_trigger
BEFORE INSERT ON Employees
FOR EACH ROW
BEGIN
    SELECT employee_sequence.NEXTVAL INTO :NEW.employee_id FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER members_trigger
BEFORE INSERT ON Members
FOR EACH ROW
BEGIN
    SELECT member_sequence.NEXTVAL INTO :NEW.member_id FROM DUAL;
END;
/


-- Function
CREATE OR REPLACE FUNCTION calculate_discount(member_id IN INT) RETURN NUMBER IS
    total_spending NUMBER(10, 2);
    loyalty_points_earned NUMBER;
    remaining_points NUMBER;
    discount_amount NUMBER(10, 2);
BEGIN
    SELECT NVL(SUM(fees_paid), 0) INTO total_spending
    FROM Bookings
    WHERE member_id = member_id;

    loyalty_points_earned := TRUNC(total_spending / 10);

    UPDATE Members
    SET loyalty_points = loyalty_points + loyalty_points_earned
    WHERE member_id = member_id;

    SELECT loyalty_points - 500 INTO remaining_points
    FROM Members
    WHERE member_id = member_id;

    IF remaining_points >= 0 THEN
        discount_amount := 50.00;

        UPDATE Members
        SET loyalty_points = remaining_points
        WHERE member_id = member_id;

        UPDATE Bookings
        SET fees_paid = fees_paid - discount_amount
        WHERE member_id = member_id
        AND booking_date >= SYSDATE 
        AND ROWNUM = 1; 

        RETURN discount_amount;
    END IF;

    RETURN 0;
END calculate_discount;
/
-- Procedure
SET SERVEROUTPUT ON;
DECLARE
    member_id INT := 1; 
    total_fees NUMBER(23, 2) := 0;

    CURSOR booking_cursor (mem_id INT) IS
        SELECT booking_id, class_id, booking_date, fees_paid
        FROM Bookings
        WHERE member_id = mem_id;

BEGIN
    FOR record IN booking_cursor(member_id) LOOP
        DBMS_OUTPUT.PUT_LINE('Booking ID: ' || record.booking_id);
        DBMS_OUTPUT.PUT_LINE('Class ID: ' || record.class_id);
        DBMS_OUTPUT.PUT_LINE('Booking Date: ' || TO_CHAR(record.booking_date, 'YYYY-MM-DD'));
        DBMS_OUTPUT.PUT_LINE('Fees Paid: $' || TO_CHAR(record.fees_paid, '9999.99'));
        DBMS_OUTPUT.PUT_LINE('----------------------');
        total_fees := total_fees + record.fees_paid;
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('Total Fees Paid by Member ' || member_id || ': $' || TO_CHAR(total_fees, '9999.99'));
END;
/




