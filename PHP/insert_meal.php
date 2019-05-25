<?php

// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');
// include('pbkdf2.compat.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {
      $UserID=(int)$_POST['ID'];
//      $Date=$_POST['Date'];
      $Contents1=$_POST['Contents1'];
      $Contents2=$_POST['Contents2'];
      $Contents3=$_POST['Contents3'];
      $Contents4=$_POST['Contents4'];
      $Contents5=$_POST['Contents5'];

      try {
        // timestamp: YYYY-MM-DD HH:MM:SS
        // codes for checking referential integrity.
        $stmt = $con->prepare("SELECT UserID FROM User WHERE UserID = :UserID");
        $stmt->bindParam(':UserID', $UserID);
        $stmt->execute();
        if( !($stmt->fetch(PDO::FETCH_ASSOC))) {
          echo("No such userID with ". $UserID);
          $e = "UserID not found.";
          throw($e);
        } else {
          $stmt = $con->prepare("INSERT INTO Meal 
            (UserID, Contents1, Contents2, Contents3, Contents4, Contents5)
             VALUES (:UserID, :Contents1, :Contents2, :Contents3, :Contents4, :Contents5)");
          $stmt->bindParam(':UserID', $UserID);
//          $stmt->bindParam(':Date', $DateRegistered);
          $stmt->bindParam(':Contents1', $Contents1);
          $stmt->bindParam(':Contents2', $Contents2);
          $stmt->bindParam(':Contents3', $Contents3);
          $stmt->bindParam(':Contents4', $Contents4);
          $stmt->bindParam(':Contents5', $Contents5);
          if($stmt->execute()){
            echo("Inserting blood glucose data successful!");
          } else {
            echo("Error inserting data values!");
          }
        } 
      }
      catch(PDOException $e) {
        die("Error!" . $e->getMessage());
      }
    }
?>