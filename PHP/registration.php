<?php

// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');
// include('pbkdf2.compat.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {
      $UserName=$_POST['ID'];
      $Password=$_POST['Password'];
      $Name=$_POST['Name'];
      $DOB=$_POST['DOB'];
      $Email=$_POST['Email'];
      $PhoneNumber=$_POST['PhoneNumber'];

      // Password hash
      $Salt = "123";
      $Password_salted = $Password . $Salt;
      $Password_hash=hash("sha256", $Password_salted);

      try {
        // Check for duplicate ID
        $stmt = $con->prepare("SELECT * FROM User WHERE UserName = :UserName");
        $stmt->bindParam(':UserName', $UserName);
        $stmt->execute();
        if($stmt->fetch(PDO::FETCH_ASSOC)){
          echo("Duplicate ID!");
          throw $e;
        }

        $stmt = $con->prepare("INSERT INTO User (Name, DOB, Email, PhoneNumber, UserName, Password) VALUES (:Name, :DOB, :Email, :PhoneNumber, :UserName, :Password)");
        $stmt->bindParam(':Name', $Name);
        $stmt->bindParam(':DOB', $DOB);
        $stmt->bindParam(':Email', $Email);
        $stmt->bindParam(':PhoneNumber', $PhoneNumber);
        $stmt->bindParam(':UserName', $UserName);
        $stmt->bindParam(':Password', $Password_hash);
        if($stmt->execute()){
          echo("Inserting successful!");
        } else {
          echo("Error inserting UserInfo.");
        }
        /* Because foreign key is not supported in MyISAM.
        $stmt = $con->prepare("INSERT INTO UserLogin (UserName, Password) VALUES (:UserName, :Password)");$stmt->bindParam(':UserName', $UserName);
        $stmt->bindParam(':Password', $Password);
        if($stmt->execute()){
          echo("Inserting User Login successful!");
        } else {
          echo("Error inserting user login!");
        } 
        */      
      }
      catch(PDOException $e) {
        die("Error!" . $e->getMessage());
      }
    }

?>