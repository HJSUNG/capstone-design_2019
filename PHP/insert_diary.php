<?php

// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');
// include('pbkdf2.compat.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {
      $UserID=(int)$_POST['ID'];
      $DateRegistered=$_POST['Date'];
      $Contents=$_POST['Contents'];

      try {
        // timestamp: YYYY-MM-DD HH:MM:SS
        // codes for checking referential integrity.
        $stmt = $con->prepare("SELECT UserID FROM User WHERE UserID = :UserID");
        $stmt->bindParam(':UserID', $UserID);
        $stmt->execute();
        if( !($stmt->fetch(PDO::FETCH_ASSOC))) {
          echo("No such userID with UserID");
          $e = "UserID not found.";
          throw($e);
        } else {
          $stmt->bindParam(':UserID', $UserID);
          $stmt = $con->prepare("INSERT INTO Diary (UserID, DateRegistered, Contents) VALUES (:UserID, :Date, :Contents)");
          $stmt->bindParam(':UserID', $UserID);
          $stmt->bindParam(':Date', $DateRegistered);
          $stmt->bindParam(':Contents', $Contents);
          if($stmt->execute()){
            echo("Inserting diary data successful!");
          } else {
            echo("Error inserting data values!");
          }
        }
        
      }
      catch(PDOException $e) {
        die("Error!" . $e->getMessage());
      }
    }

/*
      $mysqli=mysqli_connect("$host", "$username", "$password", "$dbname");

            try{
              $query_search = "SELECT * from homeseek_user WHERE ID = '".$ID."' ";
              $result = $mysqli->query($query_search);

              if($result->num_rows == 1) {
                $row=$result->fetch_array(MYSQLI_ASSOC);
                if(validate_password($PW, $row['PW'])) {
                  $_SESSION['ID']= $ID;
                  $return_nickname = $row['nickname'];
                  $return_phone = $row['phone'];
                  $return_type = $row['user_type'];

                  if(isset($_SESSION['ID'])) {
                    $successMSG = "$ID,$ID,$return_nickname,$return_phone,$return_type";
                  }
                  else {
                    $errMSG = "error";
                  }
                }else{
                  $errMSG = "error";
                }
              }else {
                $errMSG = "error";
            }
          }catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
     }
    */

