<?php

error_reporting(E_ALL);
ini_set('display_errors',1);

include('dbcon.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
      $ID=$_POST['ID'];
      $item_num=$_POST['item_num'];
      $mysqli=mysqli_connect("$host", "$username", "$password", "$dbname");

            try{
              $query_search = "SELECT * from bookmark WHERE ID = '".$ID."' AND item_num = '".$item_num."' ";
              $result = $mysqli->query($query_search);

              if($result->num_rows >= 1) {
                  $_SESSION['ID']= $ID;
                  if(isset($_SESSION['ID'])) {
                    $successMSG = "Already in bookmark !";
                  }
                  else {
                    $errMSG = "Session save failed";
                  }
                } else {
                  $errMSG = "You can add bookmark";
                }
              }catch(PDOException $e) {
                die("Database error: " . $e->getMessage());
            }
    }

?>

<?php
    if (isset($errMSG)) {
      echo "$ID,1";
    } else if (isset($successMSG)) {
      echo "$ID,22";
    }


    if (!$android)
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                ID: <input type = "text" name = "ID" />
                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>

<?php
    }
?>
