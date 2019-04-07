<?php
// https://zetawiki.com/wiki/PHP_%EC%BF%A0%ED%82%A4_%EB%A1%9C%EA%B7%B8%EC%9D%B8_%EA%B5%AC%ED%98%84
// error_reporting(E_ALL);
// ini_set('display_errors',1);

// Connect to the databse
include('dbcon.php');
// include('pbkdf2.compat.php');


    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ) {

      $ID=$_POST['ID'];

      try {
        $stmt = $con->prepare('SELECT * FROM User WHERE UserName = :ID');
        $stmt->bindParam(':ID', $ID);
        $stmt->execute();
        //$result=$stmt->fetchAll(PDO::FETCH_ASSOC);

        if($stmt->rowCount() > 0) {
          echo("ID already exists!");
        } else {
          echo("You can use this ID");
        }
      }
      catch(PDOException $e) {
        die("ID check error!" . $e->getMessage());
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

/*
    if ($successMSG != "") {
      echo $successMSG;
    } else if (isset($errMSG)) {
        echo "$errMSG";
      } else {
        echo "error";
      }



    if (!$android)
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                ID: <input type = "text" name = "ID" />
                PW: <input type = "text" name = "PW" />
                <input type = "submit" name = "submit" />
            </form>

       </body>
    </html>

<?php
    }
    */
?>