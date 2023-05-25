<?php
	$con = mysqli_connect('localhost','root','root');
	if(!$con){
		echo json_encode("connection failed");
		die("");
	}
	$db = mysqli_select_db($con,'CoffeeShop');
	if(!$db)
		die("missing database");
		
	$query = "select * from products";
	$result = mysqli_query($con, $query);
	if(!$result)
		die("something went wrong");
	
	
	$products = array();
	
	for($i=0;$i<mysqli_num_rows($result);$i++){
		$row = mysqli_fetch_assoc($result);
		$products[$i] = array("pid"=>$row['PID'], "name"=>$row['Name'], "price"=>$row['price'], "category"=>$row['category'],"description"=>$row['description']);
	}
	echo json_encode($products);

	?>

