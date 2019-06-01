<?php

// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
    
    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {
      $UserID=(int)$_POST['ID'];
      $Time=$_POST['Time'];

      try {
        // codes for checking referential integrity.
        $stmt = $con->prepare("SELECT UserID FROM User WHERE UserID = :UserID");
        $stmt->bindParam(':UserID', $UserID);
        $stmt->execute();
        if( !($stmt->fetch(PDO::FETCH_ASSOC))) {
          echo("No such userID with UserID");
          $e = "UserID not found.";
          throw($e);
        } else {
          $stmt = $con->prepare("SELECT UserID FROM MedicationAlarm WHERE (UserID = :UserID AND Time = :Time)");
          $stmt->bindParam(':UserID', $UserID);
          $stmt->bindParam(':Time', $Time);
          $stmt->execute();
          if( ($stmt->fetch(PDO::FETCH_ASSOC)) ) {
            $e = "same value";
            throw($e);
          }

          $stmt = $con->prepare("INSERT INTO MedicationAlarm (UserID, Time) VALUES (:UserID, :Time)");
          $stmt->bindParam(':UserID', $UserID);
          $stmt->bindParam(':Time', $Time);
          if($stmt->execute()){
            echo("success");
          } else {
            echo("Error inserting data values!");
          }
        }
        
      }
      catch(PDOException $e) {
        if($e = "same value")
          echo("fail");
        die("Error!" . $e->getMessage());
      }
    }
?>
