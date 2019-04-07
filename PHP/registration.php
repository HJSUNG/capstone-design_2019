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

      try {
        // Check for duplicate ID
        $stmt = $con->prepare("SELECT * FROM UserLogin WHERE UserName = :UserName");
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
        $stmt->bindParam(':Password', $Password);
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

?>